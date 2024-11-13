package com.example.pagepals1.fragments.home

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.pagepals1.LoginActivity
import com.example.pagepals1.R
import com.example.pagepals1.fragments.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.format.TextStyle

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Find the logout button in the fragment layout
        logoutBtn = view.findViewById(R.id.logout)

        // Set the onClickListener for the logout button
        logoutBtn.setOnClickListener {
            auth.signOut()
            // Navigate back to login
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("FirebaseData", "Fragment User Object: $user")
            if (user != null) {
                val userNameTextView : TextView = view.findViewById(R.id.user_name)
                userNameTextView.text = user.name

                // Update favorite genres
                val genresContainer = view?.findViewById<LinearLayout>(R.id.genre_container)
                genresContainer?.removeAllViews()

                // Adding title
                val genreTitleTextView = TextView(context)
                genreTitleTextView.text = "Favorite Genres:"
                genreTitleTextView.textSize = 18F
                val boldTypeface = Typeface.DEFAULT_BOLD
                genreTitleTextView.typeface = boldTypeface
                genresContainer?.addView(genreTitleTextView)
                genresContainer?.orientation = LinearLayout.VERTICAL

                for (genre in user.genres) {
                    val genreLayout = LayoutInflater.from(context).inflate(R.layout.genre_item, genresContainer, false)
                    val genreTextView = genreLayout.findViewById<TextView>(R.id.genre_text)
                    val genreIcon = genreLayout.findViewById<ImageView>(R.id.genre_icon)

                    genreTextView.text = genre
                    genreIcon.setBackgroundResource(R.drawable.circle_shape)

                    genresContainer?.addView(genreLayout)
                }

                // Update current clubs
                val clubContainer = view?.findViewById<LinearLayout>(R.id.club_container)
                clubContainer?.removeAllViews()

                val clubTitleTextView = TextView(context)
                clubTitleTextView.text = "Your Clubs:"
                clubTitleTextView.textSize = 18F
                val boldType = Typeface.DEFAULT_BOLD
                clubTitleTextView.typeface = boldType
                clubContainer?.addView(clubTitleTextView)
                clubContainer?.orientation = LinearLayout.VERTICAL

                for (club in user.clubs){
                    val clubLayout = LayoutInflater.from(context).inflate(R.layout.club_item, clubContainer, false)
                    val clubTextView = clubLayout.findViewById<TextView>(R.id.club_text)
                    val clubIcon = clubLayout.findViewById<ImageView>(R.id.club_icon)

                    clubTextView.text = club.clubName
                    clubIcon.setBackgroundResource(R.drawable.circle_shape)

                    clubContainer?.addView(clubLayout)
                }

            }
        }

        return view
    }
}
