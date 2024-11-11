package com.example.pagepals1.fragments.clubs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pagepals1.R
import com.example.pagepals1.data.UserSelection

class UserSelectionAdapter(
    private var userList: List<UserSelection>,
    private val onUserSelected: (String, Boolean) -> Unit
) : RecyclerView.Adapter<UserSelectionAdapter.UserViewHolder>() {

    private val selectedUsers = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val userSelection = userList[position]
        //Log.d("UserSelectionAdapter", "Binding user: ${userSelection.user.name}")

        // Mark user as selected if they are in the selectedUsers set
        val isSelected = selectedUsers.contains(userSelection.user.id) || userSelection.isSelected
        holder.bind(userSelection, isSelected)
    }

    override fun getItemCount() = userList.size

    fun updateUserList(newUserList: List<UserSelection>) {
        this.userList = newUserList
        selectedUsers.clear()
        selectedUsers.addAll(newUserList.filter { it.isSelected }.map { it.user.id }) // Retain initial selections
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameText: TextView = itemView.findViewById(R.id.userNameTextView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.selectCheckBox)

        fun bind(userSelection: UserSelection, isSelected: Boolean) {
            usernameText.text = userSelection.user.username
            checkBox.isChecked = isSelected

            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = isSelected
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onUserSelected(userSelection.user.id, isChecked)
                if (isChecked) {
                    selectedUsers.add(userSelection.user.id)
                } else {
                    selectedUsers.remove(userSelection.user.id)
                }
            }
        }
    }
}
