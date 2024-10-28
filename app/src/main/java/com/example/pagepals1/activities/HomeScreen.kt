package com.example.pagepals1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pagepals1.LoginActivity
import com.example.pagepals1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class HomeScreen : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var logoutBtn : Button
    var user : FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
        logoutBtn = findViewById(R.id.logout)

        user = auth.currentUser
        if (user == null){
            var intent: Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        logoutBtn.setOnClickListener{
            Firebase.auth.signOut()
            var intent: Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}