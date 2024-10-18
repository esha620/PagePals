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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.example.pagepals1.data.BookClubViewModel

class AddClubFragment : Fragment() {

    private lateinit var mBookClubViewModel: BookClubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_club, container, false)

        // CAUSE OF CRASH
        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            insertDataToDatabase(view)
        }

        return view
    }

    private fun insertDataToDatabase(view: View) {
        val clubName = view.findViewById<EditText>(R.id.editTextText).text.toString()
        val host = view.findViewById<EditText>(R.id.editTextText2).text.toString()

        if(inputCheck(clubName, host)) {
            // make book club object
            val bookClub = BookClub(0, clubName, host)
            mBookClubViewModel.addBookClub(bookClub)
            Toast.makeText(requireContext(), "Successfully added club!", Toast.LENGTH_LONG).show()

            // now navigate back to club page
            findNavController().navigate(R.id.clubsFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out required fields", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(clubName: String, host: String): Boolean {
        return !(TextUtils.isEmpty(clubName) && TextUtils.isEmpty(host))
    }

}