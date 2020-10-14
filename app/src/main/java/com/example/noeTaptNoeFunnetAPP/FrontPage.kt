package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class FrontPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    var isOpen = false
    val arrayList = ArrayList<CardModel>()
    val displaList = ArrayList<CardModel>()
    lateinit var myAdapter: RecyclerViewAdapter
    lateinit var preferences: SharedPreferences



    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val date = getCurrentDateTime()
        val datenow = date.toString("dd/MM/yyyy HH:mm")

            arrayList.add(CardModel("Item1", "Funnet","blå", "Funnet ute på bakken", R.drawable.bigusbrainus, datenow))
            arrayList.add(CardModel("Item2","Funnet", "Svart", "Dette er et eksempel på lengere text en passer til Cardviewen. Loreum ipsum dolaro disico nipslandat.", R.drawable.throwup, datenow))
            arrayList.add(CardModel("Katt","Funnet", "svart", "Veldig snill Katt funnet. Snakker Romansk, Heter Vladislav", R.drawable.cat, datenow))
            arrayList.add(CardModel("Esel","Funnet", "blå", "Funnet ute på bakken", R.drawable.donkey, datenow))
            arrayList.add(CardModel("Lommebok","Funnet", "svart", "Funnet ute på bakken", R.drawable.wallet, datenow))
            arrayList.add(CardModel("Hund","Funnet", "Svart", "Snill Hund funnet ute i parken uten eier", R.drawable.dogg, datenow))
            arrayList.add(CardModel("Iphone", "Funnet","hvit", "Funnet ute på bakken", R.drawable.phone, datenow))
            arrayList.add(CardModel("Hatt","Tapt", "hvit", "Jævla støgg hatt funnet utenfor Kroa", R.drawable.hat, datenow))
            arrayList.add(CardModel("Hatt","Tapt", "hvit", "Jævla støgg hatt funnet utenfor Kroa", R.drawable.hat, datenow))
            arrayList.add(CardModel("Hatt","Tapt", "hvit", "Jævla støgg hatt funnet utenfor Kroa", R.drawable.hat, datenow))
            arrayList.add(CardModel("Hatt","Tapt", "hvit", "Jævla støgg hatt funnet utenfor Kroa", R.drawable.hat, datenow))
            arrayList.add(CardModel("Hatt","Tapt", "hvit", "Jævla støgg hatt funnet utenfor Kroa", R.drawable.hat, datenow))
            displaList.addAll(arrayList)

        myAdapter = RecyclerViewAdapter(displaList, this)

        hovedListe.layoutManager = LinearLayoutManager(this)
        hovedListe.adapter = myAdapter

       preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
       val mSortSetting = preferences.getString("Sort", "Alle")

        if (mSortSetting == null) {
            sortAlle(myAdapter)

        } else if (mSortSetting == "Funnet") {
            sortFunnet(myAdapter)

        } else if (mSortSetting == "Tapt"){
            sortTapt(myAdapter)

        } else if (mSortSetting == "Alle"){
            sortAlle(myAdapter)
        }


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

    private fun sortAlle(myAdapter: RecyclerViewAdapter) {
        displaList.clear()
        displaList.addAll(arrayList)
        myAdapter.notifyDataSetChanged()

    }

    private fun sortTapt(myAdapter: RecyclerViewAdapter) {
        displaList.clear()
        arrayList.forEach {
            if (it.type == "Tapt")
                displaList.add(it)
        }
        myAdapter.notifyDataSetChanged()
    }

    private fun sortFunnet(myAdapter: RecyclerViewAdapter) {
        displaList.clear()
        arrayList.forEach {
            if (it.type.contains("Funnet"))
                displaList.add(it)
        }
        myAdapter.notifyDataSetChanged()
    }


    // Search Menu Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.searchmenu, menu)
        val menuItem = menu!!.findItem(R.id.searchBar)

        if (menuItem != null) {

            val searchView = menuItem.actionView as SearchView
            val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editText.hint = "Search..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {
                        displaList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arrayList.forEach {
                            //Search definitions
                            if (it.navn.toLowerCase(Locale.getDefault()).contains(search) or (it.farge.toLowerCase(Locale.getDefault()).contains(search))){
                                displaList.add(it)
                            }
                        }

                        hovedListe.adapter!!.notifyDataSetChanged()
                    }
                    else {
                        displaList.clear()
                        displaList.addAll(arrayList)
                        hovedListe.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.sorting) {
            sortDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortDialog() {
        val options = arrayOf("Alle", "Funnet", "Tapt")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Vis")

        builder.setItems(options) {dialog, wich ->
            if (wich == 0){
                val editor : SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Alle")
                editor.apply()
                sortAlle(myAdapter)
                Toast.makeText(this@FrontPage, "Alle", Toast.LENGTH_LONG).show()
            }
            if (wich == 1){
                val editor : SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Funnet")
                editor.apply()
                sortFunnet(myAdapter)
                Toast.makeText(this@FrontPage, "Funnet", Toast.LENGTH_LONG).show()

            }
            if (wich == 2){
                val editor : SharedPreferences.Editor = preferences.edit()
                editor.putString("Vis", "Tapt")
                editor.apply()
                sortTapt(myAdapter)
                Toast.makeText(this@FrontPage, "Tapt", Toast.LENGTH_LONG).show()

            }

        }
        builder.create().show()

    }

}