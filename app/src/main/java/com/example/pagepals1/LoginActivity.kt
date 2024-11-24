package com.example.pagepals1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.example.pagepals1.activities.HomeScreen
import com.example.pagepals1.fragments.NoNetworkFragment
import com.google.firebase.auth.FirebaseAuth
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var loginBtn : Button
    lateinit var auth: FirebaseAuth
    lateinit var regTextView: TextView
    lateinit var resetPassword : TextView

    //private lateinit var networkMonitor: NetworkMonitor

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

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        auth = FirebaseAuth.getInstance()
        regTextView = findViewById(R.id.registerNow)
        resetPassword = findViewById(R.id.forgot_password)

//        val networkMonitor = NetworkMonitor.getInstance()
//        if (!networkMonitor.isConnected()) {
//            showNoNetworkFragment()
//            return
//        }

        regTextView.setOnClickListener{
            var intent: Intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        resetPassword.setOnClickListener{
            FirebaseAuth.getInstance().sendPasswordResetEmail(usernameInput.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset email has been sent", Toast.LENGTH_SHORT).show()

                    }
                }
        }

        loginBtn.setOnClickListener{
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val networkMonitor = NetworkMonitor.getInstance()
            if (!networkMonitor.isConnected()) {
                Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.isEmpty()){
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            }

            if (password.isEmpty()){
                Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show()
            }

            val hashedPassword = hashPassword(password)

            auth.signInWithEmailAndPassword(username, hashedPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                        var intent: Intent = Intent(applicationContext, HomeScreen::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        Log.e("LoginActivity", "signInWithEmailAndPassword failed", task.exception)

                    }
                }
        }



    }
    private fun showNoNetworkFragment() {
        val noNetworkFragment = NoNetworkFragment()
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, noNetworkFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return hashedBytes.joinToString("") { it.toString(16).padStart(2, '0') }
    }
}