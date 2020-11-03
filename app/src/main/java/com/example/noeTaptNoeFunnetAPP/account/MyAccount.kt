package com.example.noeTaptNoeFunnetAPP.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.ActivityMyAccountBinding
import com.example.noeTaptNoeFunnetAPP.post_item.FormFragmentDirections
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyAccount : AppCompatActivity(), AccountNavigator {






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        supportActionBar?.title = "Min konto"
    }

    override fun navigateToUserPost() {
        val action = AccountPanelFragmentDirections.actionAccountPanelFragmentToMyPostFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

}