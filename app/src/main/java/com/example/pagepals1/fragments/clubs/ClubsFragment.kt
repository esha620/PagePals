package com.example.pagepals1.fragments.clubs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClubViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ClubsFragment : Fragment() {

    private lateinit var mBookClubViewModel: BookClubViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clubs, container, false)

        // recycler view
        val adapter = ListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // BookClub VM
        mBookClubViewModel = ViewModelProvider(this).get(BookClubViewModel::class.java)
        mBookClubViewModel.readAllData.observe(viewLifecycleOwner, Observer { club ->
            adapter.setData(club)
        })


        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            findNavController().navigate(R.id.addClubFragment)
        }


        return view
    }

}