package com.example.noeTaptNoeFunnetAPP.account

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.ActivityMyAccountBinding
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_account.*


class MyAccount : AppCompatActivity() {




    private var email : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMyAccountBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_my_account)


        supportActionBar?.title = "Min konto"

        // Inflate the layout for this fragment

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
            val domain: String? = email?.substringBefore("@")

            binding.username.text = "Hei " + domain
        }

        binding.myPostsButton.setOnClickListener{
            val intent1 = Intent(this, MyPost::class.java)
            startActivity(intent1)
        }

        sign_out_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent1 = Intent(this, FrontPage::class.java)
            startActivity(intent1)
        }

    }


}