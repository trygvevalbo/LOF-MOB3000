package com.example.noeTaptNoeFunnetAPP.account

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.Item
import com.example.noeTaptNoeFunnetAPP.ItemAdapter
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.databinding.FragmentMyPostsBinding
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_my_posts.*


class MyPostFragment : Fragment() {
    private var email : String? = null
    var itemAdapter : ItemAdapter? = null
    private val database : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMyPostsBinding>(
            inflater, R.layout.fragment_my_posts,
            container, false
        )


        recyclerSetup()
        return binding.root
    }

    fun recyclerSetup(){

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }


        var myquery = database.collection("Posts").whereEqualTo("postEmail", user)
        val query = myquery
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()


        itemAdapter = ItemAdapter(firestoreRecyclerOptions, requireContext())
        accountListe.layoutManager = LinearLayoutManager(context)
        accountListe.adapter = itemAdapter

    }

    override fun onStart() {
        super.onStart()
        itemAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        itemAdapter!!.stopListening()
    }


}