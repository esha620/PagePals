package com.example.pagepals1.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val genres: List<String> = emptyList(),
    val clubs: List<BookClub> = emptyList()
) {

    fun getFirebaseUser(id: String): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("users").child(id)
    }

    fun updateName(context: Context, userId: String, newName: String) {
        val userRef = getFirebaseUser(userId)
        userRef.child("name").setValue(newName)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Unable to update name",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    fun updateGenres(context: Context, userId: String, updatedGenres: List<String>) {
        val userRef = getFirebaseUser(userId)
        userRef.child("genres").setValue(updatedGenres)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Unable to update favorite genres",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

}
