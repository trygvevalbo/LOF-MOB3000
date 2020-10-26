package com.example.noeTaptNoeFunnetAPP

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class FrontPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database : FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference:CollectionReference = database.collection("Posts")


    var itemAdapter : ItemAdapter? = null;
    var isOpen = false

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); setTheme(R.style.AppTheme); AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()


        recyclerSetup()
        preferancesSetup()

    }


    fun recyclerSetup(){
        var thisRef = database.collection("Posts")
        val query = thisRef
        val firestoreRecyclerOptions : FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(query, Item::class.java)
            .build()


        itemAdapter = ItemAdapter(firestoreRecyclerOptions)
        hovedListe.layoutManager = LinearLayoutManager(this)
        hovedListe.adapter = itemAdapter
    }


    override fun onStart() {
        super.onStart()
        itemAdapter!!.startListening()

    }

    override fun onDestroy() {
        super.onDestroy()
        itemAdapter!!.stopListening()
    }
    fun preferancesSetup() {
        preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
        val fobOpen = AnimationUtils.loadAnimation(this, R.anim.fob_open)
        val fobClose = AnimationUtils.loadAnimation(this, R.anim.fob_close)
        val fobClockwise = AnimationUtils.loadAnimation(this, R.anim.spin_clockwise)
        val fobCounterclockwise = AnimationUtils.loadAnimation(this, R.anim.spin_counterclockwise)

        submitButton.setOnClickListener {
            if (isOpen) {
                taptKnapp.startAnimation(fobClose)
                funnetKnapp.startAnimation(fobClose)
                submitButton.startAnimation(fobClockwise)

                isOpen = false
            }
            else {
                taptKnapp.startAnimation(fobOpen)
                funnetKnapp.startAnimation(fobOpen)
                submitButton.startAnimation(fobCounterclockwise)

                taptKnapp.isClickable
                funnetKnapp.isClickable

                isOpen = true
            }
            taptKnapp.setOnClickListener{
                val user = Firebase.auth.currentUser
                if (user != null){
                    val intent1 = Intent(this, PostLostItem::class.java)
                    startActivity(intent1)
                } else {
                    val intent1 = Intent(this, LoginActivity::class.java)
                    startActivity(intent1)

                }
            }

            funnetKnapp.setOnClickListener{
                // val user = Firebase.auth.currentUser
                //if (user != null){
                val intent1 = Intent(this, PostFoundItem::class.java)
                startActivity(intent1)
                //} else {
                //  val intent1 = Intent(this, LoginActivity::class.java)
                //startActivity(intent1)

                //}
            }
        }
    }

    // Search Menu Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.searchmenu, menu)
        val menuItem = menu!!.findItem(R.id.searchBar)

        if (menuItem != null) {



        return super.onCreateOptionsMenu(menu)
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.sorting) {
           // sortDialog()
        }
        return super.onOptionsItemSelected(item)
    }




}