package com.example.pagepals1.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pagepals1.R
import com.example.pagepals1.fragments.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class EditProfileFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val auth = FirebaseAuth.getInstance()
        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        val backButton = view.findViewById<Button>(R.id.back_button)

        return view

    }
}