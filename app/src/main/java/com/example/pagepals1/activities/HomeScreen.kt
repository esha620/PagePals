package com.example.pagepals1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pagepals1.LoginActivity
import com.example.pagepals1.R
import com.example.pagepals1.data.User
import com.example.pagepals1.fragments.UserViewModel
import com.example.pagepals1.fragments.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeScreen : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var user : FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()

        user = auth.currentUser
        if (user == null){
            var intent: Intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        fetchUserData(user!!.uid) { retrievedUser ->
            Log.d("FirebaseData", "callback received, abt to run viewModel.setUser $retrievedUser")
            viewModel.setUser(retrievedUser)
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

    private fun fetchUserData(userId: String, callback: (User) -> Unit) {
        val databaseReference = User().getFirebaseUser(userId)
        Log.d("FirebaseData", "Firebase reference is $databaseReference")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("FirebaseData", "Data Snapshot: ${dataSnapshot.value}")
                val user = dataSnapshot.getValue(User::class.java)
                Log.d("FirebaseData", "Retrieved User Object: $user")

                if (user != null) {
                    callback(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("FirebaseData", "Error fetching user data: ${databaseError.message}")
            }
        })
    }

}