package com.example.lof_mob3000

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lof_mob3000.post_item.PostFoundItem
import com.example.lof_mob3000.post_item.PostLostItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class FrontPage : AppCompatActivity() {

    var isOpen = false

    val arrayList = ArrayList<CardModel>()
    val displaList = ArrayList<CardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)

        arrayList.add(CardModel("Item1", "blå", "Funnet ute på bakken", R.drawable.bigusbrainus))
        arrayList.add(CardModel("Item2", "Svart", "Dette er et eksempel på lengere text en passer til Cardviewen. Loreum ipsum dolaro disico nipslandat.", R.drawable.throwup))
        arrayList.add(CardModel("Item3", "hvit", "Funnet ute på bakken", R.drawable.cat))
        arrayList.add(CardModel("Item4", "blå", "Funnet ute på bakken", R.drawable.donkey))
        arrayList.add(CardModel("Item5", "ingen", "Funnet ute på bakken", R.drawable.donkey))
        arrayList.add(CardModel("Item6", "rød", "Funnet ute på bakken", R.drawable.donkey))
        arrayList.add(CardModel("Item6", "rød", "Funnet ute på bakken", R.drawable.donkey))
        arrayList.add(CardModel("Item6", "rød", "Funnet ute på bakken", R.drawable.ic_launcher_background))
        displaList.addAll(arrayList)

        val myAdapter = RecyclerViewAdapter(displaList, this)

        hovedListe.layoutManager = LinearLayoutManager(this)
        hovedListe.adapter = myAdapter


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

                    val intent1 = Intent(this, PostFoundItem::class.java)
                    startActivity(intent1)
            }

            funnetKnapp.setOnClickListener{
                val intent1 = Intent(this, PostLostItem::class.java)
                startActivity(intent1)
            }
        }
    }

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
        return super.onOptionsItemSelected(item)
    }

}