package com.example.pagepals1.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pagepals1.NetworkMonitor
import com.example.pagepals1.R

class NoNetworkFragment : Fragment() {

    private lateinit var retryButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_network, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retryButton = view.findViewById(R.id.retryBtn)
        retryButton.setOnClickListener {
            val networkMonitor = NetworkMonitor.getInstance()

            if (networkMonitor.isConnected()) {
                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(context, "Still no connection. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
