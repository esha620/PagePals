package com.example.pagepals1.fragments.clubs

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.example.pagepals1.data.BookClubViewModel
import com.example.pagepals1.data.User
import com.example.pagepals1.data.UserSelection
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mBookClubViewModel: BookClubViewModel
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userSelectionAdapter: UserSelectionAdapter
    private val selectedUserIds = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)

        // Set current club name
        view.findViewById<EditText>(R.id.updateClubName).setText(args.currentBookClub.clubName)

        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadUsers(view)

        view.findViewById<Button>(R.id.updateBtn).setOnClickListener {
            updateItem(view)
        }

        view.findViewById<FloatingActionButton>(R.id.deleteBtn).setOnClickListener {
            deleteClub()
        }

        return view
    }

    private fun loadUsers(view: View) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val userList = mutableListOf<UserSelection>()

        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (userSnapshot in snapshot.children) {
                    val id = userSnapshot.child("id").getValue(String::class.java)
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val genresSnapshot = userSnapshot.child("genres")
                    val genres = genresSnapshot.children.mapNotNull { it.getValue(String::class.java) }

                    if (id != null && name != null && username != null) {
                        val user = User(id, name, username, "", genres)
                        val isSelected = args.currentBookClub.members.contains(id)
                        if (isSelected) selectedUserIds.add(id)
                        userList.add(UserSelection(user, isSelected))
                    }
                }

                // Initialize adapter with selected members
                userSelectionAdapter = UserSelectionAdapter(userList) { userId, isSelected ->
                    if (isSelected) {
                        selectedUserIds.add(userId)
                    } else {
                        selectedUserIds.remove(userId)
                    }
                }

                userRecyclerView.adapter = userSelectionAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error loading users: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateItem(view: View) {
        val clubName = view.findViewById<EditText>(R.id.updateClubName).text.toString()
        val hostId = args.currentBookClub.hostId

        if (inputCheck(clubName, hostId)) {
            val updatedClub = BookClub(
                clubId = args.currentBookClub.clubId,
                clubName = clubName,
                hostId = hostId,
                members = listOf(hostId) + selectedUserIds  // Include host and selected members
            )

            mBookClubViewModel.updateBookClub(updatedClub)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_clubsFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(clubName: String, host: String): Boolean {
        return !(TextUtils.isEmpty(clubName) || TextUtils.isEmpty(host))
    }

    private fun deleteClub() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mBookClubViewModel.deleteBookClub(args.currentBookClub)
            Toast.makeText(requireContext(), "Successfully removed", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_clubsFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${args.currentBookClub.clubName}?")
        builder.create().show()
    }
}
