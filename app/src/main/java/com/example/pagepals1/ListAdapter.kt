package com.example.pagepals1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.data.BookClub

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var clubList = emptyList<BookClub>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //holder.setIsRecyclable(false)

        val currentItem = clubList[position]
        holder.itemView.findViewById<TextView>(R.id.textView4).text = currentItem.clubId.toString()
        holder.itemView.findViewById<TextView>(R.id.clubName_txt).text = currentItem.clubName
        holder.itemView.findViewById<TextView>(R.id.hostName_txt).text = currentItem.host

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