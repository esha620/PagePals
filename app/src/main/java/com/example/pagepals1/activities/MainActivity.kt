package com.example.pagepals1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import com.example.pagepals1.R

class MainActivity : AppCompatActivity() {
    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var loginBtn : Button

    // nav controller
    private lateinit var navController: NavController

    private val correctUsername = "user123"
    private val correctPassword = "password"

    // Methods for logging purposed
    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause triggered") // Checkpoint 3 logging
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume triggered") // Checkpoint 3 logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)

        loginBtn.setOnClickListener{
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username == correctUsername && password == correctPassword){
                val intent = Intent(this, HomeScreen::class.java)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                Log.i("Login", "Login successful: Username: $username")

            }else{
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                Log.i("Login", "Login failed: Username: $username, Password: $password")
            }


        }

    }



}