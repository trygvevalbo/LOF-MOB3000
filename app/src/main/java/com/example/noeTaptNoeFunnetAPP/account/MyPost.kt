package com.example.noeTaptNoeFunnetAPP.account


import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.Item
import com.example.noeTaptNoeFunnetAPP.ItemAdapter
import com.example.noeTaptNoeFunnetAPP.R


import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_post.*
import kotlinx.android.synthetic.main.activity_my_post.accountListe


class MyPost : AppCompatActivity() {

    private var email : String? = null

    var itemAdapter : ItemAdapter? = null
    private val database : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)
        supportActionBar?.title = "Mine poster"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerSetup()
    }


    private fun recyclerSetup(){
        var thisRef = database.collection("Posts")

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {
                email = profile.email
            }
        }


        var myquery = database.collection("Posts").whereEqualTo("userEmail", email)
        val query = myquery
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()



        itemAdapter = ItemAdapter(firestoreRecyclerOptions,this)
        accountListe.layoutManager = LinearLayoutManager(this)

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
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}