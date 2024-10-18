package com.example.pagepals1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.data.BookClub

class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var clubList = emptyList<BookClub>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clubList[position]
        holder.itemView.findViewById<TextView>(R.id.textView4).text = currentItem.clubId.toString()
        holder.itemView.findViewById<TextView>(R.id.clubName_txt).text = currentItem.clubName
        holder.itemView.findViewById<TextView>(R.id.hostName_txt).text = currentItem.host
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