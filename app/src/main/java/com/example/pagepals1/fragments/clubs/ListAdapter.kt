package com.example.pagepals1.fragments.clubs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var clubList = emptyList<BookClub>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = clubList[position]
        holder.itemView.findViewById<TextView>(R.id.clubName_txt).text = currentItem.clubName

        // Fetch host name from Firebase using hostId
        val hostId = currentItem.hostId
        val hostNameTextView = holder.itemView.findViewById<TextView>(R.id.hostName_txt)

        // Check if hostId is not null before querying Firebase
        if (hostId != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(hostId)
            userRef.get().addOnSuccessListener { snapshot ->
                // Extract host name from snapshot
                val hostName = snapshot.child("name").getValue(String::class.java)
                hostNameTextView.text = "Host Name: ${hostName ?: "Unknown"}"
            }.addOnFailureListener {
                hostNameTextView.text = "Host Name: Unknown"
            }
        } else {
            hostNameTextView.text = "Host Name: Unknown"
        }

        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
            val action = ClubsFragmentDirections.actionClubsFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

    }

    fun setData(bookClub: List<BookClub>) {
        this.clubList = bookClub
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        Log.d("club: ", clubList.size.toString())
        return clubList.size
    }
}
