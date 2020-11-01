package com.example.noeTaptNoeFunnetAPP.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.ActivityMyAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyAccount : AppCompatActivity() {



    private lateinit var binding: ActivityMyAccountBinding
    private var email : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_account)

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                 email = profile.email
            }
            val domain: String? = email?.substringBefore("@")

            binding.username.text = "Hei " + domain
        }

        binding.myPostsButton.setOnClickListener{

        }

        supportActionBar?.title = "Dine funn og tap"

    }


}