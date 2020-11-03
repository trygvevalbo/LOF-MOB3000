package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class FrontPage : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var postRef = database.collection("Posts")
    private var itemAdapter: ItemAdapter? = null

    var myquery  = postRef.orderBy("postTime")
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); setTheme(R.style.AppTheme); AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        ); setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()


        preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
        val mSortSetting = preferences.getString("Sort", "Alle")

        if (mSortSetting == null) {
            recyclerSetup()
            preferancesSetup()

        } else if (mSortSetting == "Funnet") {
            sortFunnet()

        } else if (mSortSetting == "Tapt") {
            sortTapt()

        } else if (mSortSetting == "Alle") {
            sortAlle()
        }

        recyclerSetup()
        preferancesSetup()
    }


    fun recyclerSetup() {
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(myquery, Item::class.java)
            .build()


        itemAdapter = ItemAdapter(firestoreRecyclerOptions, this)
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
            } else {
                taptKnapp.startAnimation(fobOpen)
                funnetKnapp.startAnimation(fobOpen)
                submitButton.startAnimation(fobCounterclockwise)

                taptKnapp.isClickable
                funnetKnapp.isClickable

                isOpen = true
            }
            taptKnapp.setOnClickListener {
                val user = Firebase.auth.currentUser
                if (user != null) {
                    val intent1 = Intent(this, PostLostItem::class.java)
                    startActivity(intent1)
                } else {
                    val intent1 = Intent(this, LoginActivity::class.java)
                    startActivity(intent1)

                }
            }

            funnetKnapp.setOnClickListener {
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

    fun sortAlle() {
        recyclerSetup()
        onStart()
    }

    fun sortTapt() {
        myquery = database.collection("Posts").whereEqualTo("postType", "Tapt")
        recyclerSetup()
        onStart()
    }

    fun sortFunnet() {
        myquery = database.collection("Posts").whereEqualTo("postType", "Funnet")
        recyclerSetup()
        onStart()
    }

    // Search Menu Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.searchmenu, menu)
        val menuItem = menu!!.findItem(R.id.searchBar)

        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            val editText = searchView.findViewById<EditText>(R.id.search_src_text)
            editText.hint = "Search..."

        }

        return super.onCreateOptionsMenu(menu)

    }

    // Sorting Menu Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.sorting) {
            sortDialog()
        }
        return super.onOptionsItemSelected(item)
    }


    // Sorting Dialog handler
    private fun sortDialog() {
        val options = arrayOf("Alle", "Funnet", "Tapt")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Vis")
        builder.setItems(options) { dialog, wich ->
            if (wich == 0) {
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Alle")
                editor.apply()
                sortAlle()
                Toast.makeText(this@FrontPage, "Alle", Toast.LENGTH_LONG).show()
            }
            if (wich == 1) {
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Funnet")
                editor.apply()
                sortFunnet()
                Toast.makeText(this@FrontPage, "Funnet", Toast.LENGTH_LONG).show()
            }
            if (wich == 2) {
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Tapt")
                editor.apply()
                sortTapt()
                Toast.makeText(this@FrontPage, "Tapt", Toast.LENGTH_LONG).show()
            }
        }
        builder.create().show()
    }


}