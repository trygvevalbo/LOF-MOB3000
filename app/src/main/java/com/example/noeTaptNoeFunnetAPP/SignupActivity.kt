package com.example.noeTaptNoeFunnetAPP

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        submit_signup_btn.setOnClickListener {
            signupUser()
        }

    }

   private fun signupUser () {

            if (signup_email.text.toString().isEmpty()) {
                signup_email.error = "Venligst tast inn Epost"
                signup_email.requestFocus()
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(signup_email.text.toString()).matches()) {
                signup_email.error = "Venligst tast inn en gyldig Epost adresse"
                signup_email.requestFocus()
                return
            }
            if (signup_password.text.toString().isEmpty()) {
                signup_password.error = "Venligst tast inn passord"
                signup_password.requestFocus()
                return
            }

       auth.createUserWithEmailAndPassword(signup_email.text.toString(), signup_password.text.toString())
           .addOnCompleteListener(this) { task ->
               if (task.isSuccessful) {
                  startActivity(Intent(this, LoginActivity::class.java))
                   finish()
               } else {
                   // If sign in fails, display a message to the user.
                   Toast.makeText(baseContext, "Sign-up failed!",
                       Toast.LENGTH_SHORT).show()
               }

               // ...
           }

        }
    }

