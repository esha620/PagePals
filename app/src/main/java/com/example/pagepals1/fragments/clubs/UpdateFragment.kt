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
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.example.pagepals1.data.BookClubViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mBookClubViewModel: BookClubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)

        view.findViewById<EditText>(R.id.updateClubName).setText(args.currentBookClub.clubName)
        view.findViewById<EditText>(R.id.updateHostName).setText(args.currentBookClub.host)

        view.findViewById<Button>(R.id.updateBtn).setOnClickListener {
            updateIten(view)
        }

        view.findViewById<FloatingActionButton>(R.id.deleteBtn).setOnClickListener {
            deleteClub()
        }

        return view
    }

    private fun updateIten(view: View) {
        val clubName = view.findViewById<EditText>(R.id.updateClubName).text.toString()
        val hostName = view.findViewById<EditText>(R.id.updateHostName).text.toString()
        val cityName = view.findViewById<EditText>(R.id.updateCityName).text.toString()

        if(inputCheck(clubName,hostName)) {
            val updatedClub = BookClub(args.currentBookClub.clubId, clubName, hostName, cityName)
            mBookClubViewModel.updateBookClub(updatedClub)
            Toast.makeText(requireContext(), "Successfully Updated!", Toast.LENGTH_SHORT).show()
            // navigate back to clubs pg
            findNavController().navigate(R.id.action_updateFragment_to_clubsFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun inputCheck(clubName: String, host: String): Boolean {
        return !(TextUtils.isEmpty(clubName) && TextUtils.isEmpty(host))
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