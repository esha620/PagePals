package com.example.pagepals1.fragments.clubs

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.example.pagepals1.data.BookClubViewModel
import com.example.pagepals1.data.User
import com.example.pagepals1.data.UserSelection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddClubFragment : Fragment() {

    private lateinit var mBookClubViewModel: BookClubViewModel
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userSelectionAdapter: UserSelectionAdapter
    private val selectedUserIds = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_club, container, false)

        // CAUSE OF CRASH
        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)

        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadUsers(view)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
    }

    private fun loadUsers(view: View) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        val userList = mutableListOf<UserSelection>()

        // Fetch users from Firebase
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Clear existing list

                // Loop through the snapshot and add each user to the list
                for (userSnapshot in snapshot.children) {
                    val id = userSnapshot.child("id").getValue(String::class.java)
                    val name = userSnapshot.child("name").getValue(String::class.java)
                    val username = userSnapshot.child("username").getValue(String::class.java)
                    val password = userSnapshot.child("password").getValue(String::class.java)
                    val genresSnapshot = userSnapshot.child("genres")
                    val genres = mutableListOf<String>()
                    for (genreSnapshot in genresSnapshot.children) {
                        val genre = genreSnapshot.getValue(String::class.java)
                        genre?.let { genres.add(it) }
                    }

                    if (id != null && name != null && username != null && password != null) {
                        val user = User(id, name, username, password, genres)
                        userList.add(UserSelection(user))  // Wrap in UserSelection
                    }
                }
                Log.d("LoadedUsers", "User List: $userList")


                // Initialize Adapter with loaded users
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



    private fun insertDataToDatabase(view: View) {
        val clubName = view.findViewById<EditText>(R.id.editTextText).text.toString()
        //val host = view.findViewById<EditText>(R.id.editTextText2).text.toString()
        val city = view.findViewById<EditText>(R.id.editTextCity).text.toString()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val hostId = currentUser?.uid
        

        if (inputCheck(clubName) && hostId != null && inputCheck(city)) {
            val members = listOf(hostId) + selectedUserIds

            val bookClub = BookClub(
                clubId = 0,
                clubName = clubName,
                city = city,
                hostId = hostId,
                members = members
            )

            // Add the club to the ViewModel and show success message
            mBookClubViewModel.addBookClub(bookClub)
            Toast.makeText(requireContext(), "Successfully added club!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.clubsFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out required fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(clubName: String): Boolean {
        return !(TextUtils.isEmpty(clubName))
    }
}
