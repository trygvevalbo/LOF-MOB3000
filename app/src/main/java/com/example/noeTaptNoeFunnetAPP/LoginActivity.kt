package com.example.noeTaptNoeFunnetAPP

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*

class LoginActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var firstTimeUser = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        buttonListener()
        signup_btn.setOnClickListener{
            val intent1 = Intent(this, SignupActivity::class.java)
            startActivity(intent1)
        }

    }

    private fun buttonListener(){
        login_btn.setOnClickListener {
            loginUser()
        }

        signup_btn.setOnClickListener{
            val intent1 = Intent(this, SignupActivity::class.java)
            startActivity(intent1)
        }
    }

    private fun loginUser(){
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Toast.makeText(this@LoginActivity,"Du er nå logget inn", Toast.LENGTH_SHORT).show()
                        val i = Intent(this@LoginActivity, FrontPage::class.java)
                        startActivity(i)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
            }
        }
    }

    public override fun onStart() {
        auth = Firebase.auth
        super.onStart()
        checkIfUserIsLoggedIn()
        //Is User signed in check
      //  val currentUser = auth.currentUser
    //    updateUI(currentUser)
    }

    fun updateUI(currentUser : FirebaseUser?) {

    }
    private fun checkIfUserIsLoggedIn(){
        if(auth.currentUser != null){
            val i = Intent(this@LoginActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}