package com.example.noeTaptNoeFunnetAPP.account

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentAccountPanelBinding
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentMyPostsBinding
import com.example.noeTaptNoeFunnetAPP.post_item.AppNavigator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class AccountPanelFragment : Fragment() {
    private var email : String? = null
    private lateinit var AccountNavigator: AccountNavigator

    override fun onAttach(context: Context) { //få context til å senere kunne sende til user posts
        super.onAttach(context)
        AccountNavigator = context as AccountNavigator
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentAccountPanelBinding>(
            inflater, R.layout.fragment_account_panel,
            container, false)
        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
            val domain: String? = email?.substringBefore("@")

            binding.username.text = "Hei " + domain
        }

        binding.myPostsButton.setOnClickListener{
            AccountNavigator.navigateToUserPost()
        }


        return binding.root

    }
}