package com.example.pagepals1

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.example.pagepals1.data.User
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pagepals1.activities.HomeScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var nameInput : EditText
    lateinit var registerBtn : Button
    lateinit var auth: FirebaseAuth
    lateinit var loginTextView: TextView
    private val selectedGenres = mutableListOf<String>()

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            var intent: Intent = Intent(applicationContext, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_activiy)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val genres = listOf("Fantasy", "Sci-Fi", "Romance", "Mystery", "Thriller")
        val genreContainer = findViewById<LinearLayout>(R.id.genre_container)

        genres.forEach { genre ->
            val checkBox = CheckBox(this)
            checkBox.text = genre
            checkBox.setTextColor(Color.WHITE)
            checkBox.buttonTintList = ColorStateList.valueOf(Color.WHITE)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedGenres.add(genre)
                } else {
                    selectedGenres.remove(genre)
                }
            }
            genreContainer.addView(checkBox)
        }

        nameInput = findViewById(R.id.name_input)
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        registerBtn = findViewById(R.id.register_btn)
        auth = FirebaseAuth.getInstance()
        loginTextView = findViewById(R.id.loginNow)

        loginTextView.setOnClickListener{
            var intent: Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        registerBtn.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val name = nameInput.text.toString()
            val intent = Intent(applicationContext, LoginActivity::class.java)


            if (username.isEmpty()){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            }

            if (password.isEmpty()){
                Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show()
            }
            if (password.length < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            }

            if (name.isEmpty()){
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        val user = User(
                            id = firebaseUser?.uid ?: "",
                            name = nameInput.text.toString(),
                            username = usernameInput.text.toString(),
                            password = passwordInput.text.toString(),
                            genres = selectedGenres
                        )

                        val databaseReference = FirebaseDatabase.getInstance().getReference("users/${firebaseUser?.uid}")
                        databaseReference.setValue(user)
                            .addOnCompleteListener { item ->
                                if (item.isSuccessful) {
                                    startActivity(intent)
                                }
                            }

                        Toast.makeText(
                            baseContext,
                            "Account created.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //startActivity(intent) // this may need to go straight to home page instead of to login
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }
    }
}