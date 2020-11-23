package com.example.noeTaptNoeFunnetAPP

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noeTaptNoeFunnetAPP.account.MyAccount
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.example.noeTaptNoeFunnetAPP.post_item.location.LocationUtil
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.fonfon.kgeohash.GeoHash
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class FrontPage : AppCompatActivity() {
    // Lateinit and Vars needed later
    lateinit var preferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var postRef = database.collection("Posts")
    private var itemAdapter: ItemAdapter? = null

    private var PERMISSION_ID  : Int= 1000
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest


    // Query for database and isOpen for fob-button
    var myquery = postRef.orderBy("postTime")
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); setTheme(R.style.AppTheme); AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_YES
        ); setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        //sharedPrefs for sorting
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

        } else if (mSortSetting == "Lokasjon") {
        sortLokasjon()
    }

        recyclerSetup()
        preferancesSetup()
        checkPermission()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val locationUtil = LocationUtil(this)
        if(locationUtil.getUserLocation()){
            getNewLocation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation =p0.lastLocation


           //Toast.makeText(this@FrontPage,lastLocation.latitude.toString() + lastLocation.longitude.toString(),Toast.LENGTH_LONG).show()

            val location = Location("geohash")
            location.latitude = lastLocation.latitude
            location.longitude = lastLocation.longitude

            val geoHashLocation = GeoHash(location, 5)
            Toast.makeText(this@FrontPage,geoHashLocation.toString(),Toast.LENGTH_LONG).show()

        }
    }

    //Main function for FirestoreDatabase
    fun recyclerSetup() {
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Item> = FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(myquery, Item::class.java)
                .build()


        //Define adapter
        itemAdapter = ItemAdapter(firestoreRecyclerOptions, this)
        hovedListe.layoutManager = LinearLayoutManager(this)
        hovedListe.adapter = itemAdapter
    }

    //Attach adapter
    override fun onStart() {
        super.onStart()
        itemAdapter!!.startListening()
        invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
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

                taptKnapp.visibility = View.GONE
                funnetKnapp.visibility = View.GONE
                taptKnapp.isEnabled = false
                funnetKnapp.isEnabled = false

                isOpen = false
            } else {
                taptKnapp.startAnimation(fobOpen)
                funnetKnapp.startAnimation(fobOpen)
                submitButton.startAnimation(fobCounterclockwise)

                taptKnapp.isEnabled = true
                funnetKnapp.isEnabled = true


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
                val user = Firebase.auth.currentUser
                if (user != null) {
                    val intent1 = Intent(this, PostFoundItem::class.java)
                    startActivity(intent1)
                } else {
                    val intent1 = Intent(this, LoginActivity::class.java)
                    startActivity(intent1)

                }
            }
        }
    }
    // Sorting functions. Ajusts Query to the relevant sorting
    fun sortAlle() {
        myquery = postRef.orderBy("postTime")
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
    fun sortLokasjon() {
        

    }


    // Search Menu Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val user = Firebase.auth.currentUser
        menuInflater.inflate(R.menu.searchmenu, menu)
        //Define searchbar
        val menuItem = menu!!.findItem(R.id.searchBar)

        //Second Def for ExpandListener
        val searchView = menuItem.actionView as SearchView

        //Listener for searchView expand and collapse
        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    //OnQueryTextSubmit runs recyclerSetup with new Query
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        recyclerSetup()
                        onStart()
                        return true
                    }
                    //Ajust Query to search-value
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText!!.isNotEmpty()) {
                            myquery = database
                                .collection("Posts")
                                .whereArrayContains("keyWords", newText.toString()
                                    .trim().toLowerCase(Locale.getDefault()))
                        }
                        return true
                    }
                })
                return true
            }

            // Listens on collapse and resets itemView
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                sortAlle()
                recyclerSetup()
                onStart()
                return true
            }
        })


        //Account Button
        val accountButton = menu!!.findItem(R.id.accountButton)
        accountButton.isVisible = user != null

        accountButton.setOnMenuItemClickListener {
            val intent1 = Intent(this, MyAccount::class.java)
            startActivity(intent1)
            return@setOnMenuItemClickListener false
        }
        return true
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
        val options = arrayOf("Alle", "Funnet", "Tapt", "Lokasjon")
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
            if (wich == 3) {
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Lokasjon")
                editor.apply()
                sortLokasjon()
                Toast.makeText(this@FrontPage, "Nærmeste Annonser", Toast.LENGTH_LONG).show()
            }
        }
        builder.create().show()
    }



    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )

    }

    private fun checkPermission(){
        if(
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED  ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           requestPermission()
        }

    }




}