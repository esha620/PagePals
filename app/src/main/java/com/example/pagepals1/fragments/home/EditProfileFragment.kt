package com.example.pagepals1.fragments.home

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pagepals1.R
import com.example.pagepals1.data.BookClub
import com.example.pagepals1.data.User
import com.example.pagepals1.fragments.UserViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth

class EditProfileFragment : Fragment(){

    val genres = listOf("Fantasy", "Sci-Fi", "Romance", "Mystery", "Thriller")
    var selectedGenres = mutableListOf<String>()
    var clubs = mutableListOf<BookClub>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val nameLabel = view.findViewById<TextView>(R.id.user_name_label)
        val usernameLabel = view.findViewById<TextView>(R.id.username_label)
        val nameEditText = view.findViewById<EditText>(R.id.name_edit_text)
        val usernameEditText = view.findViewById<EditText>(R.id.username_edit_text)
        val resetButton = view.findViewById<Button>(R.id.password_reset)
        val genreContainer = view.findViewById<LinearLayout>(R.id.genre_container)
        val clubContainer = view.findViewById<LinearLayout>(R.id.club_container)
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val backButton = view.findViewById<Button>(R.id.back_button)

        // Set up back conditions
        backButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val fragment = HomeFragment()
            transaction.replace(R.id.main, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("FirebaseData", "Fragment User Object: $user")

            // Set Current Values and send out reset email
            nameLabel.text = "Name: ${user.name}"
            usernameLabel.text = "Email: ${user.username}"
            resetButton.setOnClickListener {
                FirebaseAuth.getInstance().sendPasswordResetEmail(user.username)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this.context, "Reset email has been sent", Toast.LENGTH_SHORT).show()

                        }
                    }
            }

            // Populate Genres
            selectedGenres = user.genres.toMutableList()
            genres.forEach { genre ->
                val checkBox = CheckBox(this.context)
                checkBox.text = genre
                checkBox.setTextColor(Color.BLACK)
                checkBox.buttonTintList = ColorStateList.valueOf(Color.BLACK)
                checkBox.isChecked = selectedGenres.contains(genre)
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedGenres.add(genre)
                    } else {
                        selectedGenres.remove(genre)
                    }
                }
                genreContainer.addView(checkBox)
            }

            // Populate Clubs
            clubs = user.clubs.toMutableList()
            clubContainer.orientation = LinearLayout.VERTICAL
            clubs.forEach { club ->
                val clubTextView = TextView(context)
                clubTextView.text = club.clubName
                clubTextView.textSize = 18F
                val boldType = Typeface.DEFAULT_BOLD
                clubTextView.typeface = boldType
                clubContainer?.addView(clubTextView)
            }

            // Save changed values
            saveButton.setOnClickListener {
                var ref = user.getFirebaseUser(user.id)
                var updatedUser = mutableMapOf<String, Any>()
                updatedUser["id"] = user.id
                updatedUser["name"] = user.name
                if (nameEditText.text.isNotEmpty()) updatedUser["name"] = nameEditText.text.toString()
                updatedUser["username"] = user.username
                if (usernameEditText.text.isNotEmpty()) updatedUser[ "username"] = usernameEditText.text.toString()
                updatedUser["password"] = user.password
                updatedUser["genres"] = selectedGenres
                updatedUser["clubs"] = user.clubs

                ref.updateChildren(updatedUser)

                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                val fragment = HomeFragment()
                transaction.replace(R.id.main, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        return view

    }
}