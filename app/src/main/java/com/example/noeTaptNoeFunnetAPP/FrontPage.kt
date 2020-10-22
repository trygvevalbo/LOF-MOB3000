package com.example.noeTaptNoeFunnetAPP

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noeTaptNoeFunnetAPP.post_item.PostFoundItem
import com.example.noeTaptNoeFunnetAPP.post_item.PostLostItem
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FrontPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var recyclerView: RecyclerView
    lateinit var ref:DatabaseReference


    var isOpen = false
    val arrayList = ArrayList<Item>()
    val displaList = ArrayList<Item>()

    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        recyclerView=findViewById(R.id.hovedListe)
        ref= FirebaseDatabase.getInstance().reference.child("Posts")
        recyclerView.layoutManager=LinearLayoutManager(this)


        val option=FirebaseRecyclerOptions.Builder<Item>().setQuery(ref, Item::class.java).build()

        val firebaseRecyclerAdapter= object :FirebaseRecyclerAdapter<Item, ItemViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
                val itemView =
                    LayoutInflater.from(this@FrontPage).inflate(R.layout.liste, parent, false)
                return ItemViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Item) {
                val refId = getRef(position).key.toString()
                ref.child(refId).addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        holder.rNameOfItem.text = model.nameOfItem
                        holder.rColorOfFound.text = model.colorOfFound
                        //holder.rContact.text = model.contact
                        holder.rDescriptionOfFound.text = model.descriptionOfFound
                        holder.rTime.text = model.time
                        holder.rTypeOfPost.text = model.typeOfPost
                        Glide.with(holder.itemView.context).load(model.postImage).into(holder.rImage);

                    }

                })
            }
        }
        recyclerView.adapter=firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()




        preferences = getSharedPreferences("My_Pref", Context.MODE_PRIVATE)
        val mSortSetting = preferences.getString("Sort", "Alle")

        if (mSortSetting == null) {
            sortAlle(firebaseRecyclerAdapter)

        } else if (mSortSetting == "Funnet") {
            sortFunnet(firebaseRecyclerAdapter)

        } else if (mSortSetting == "Tapt"){
            sortTapt(firebaseRecyclerAdapter)

        } else if (mSortSetting == "Alle"){
            sortAlle(firebaseRecyclerAdapter)
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

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var rImage: ImageView = itemView.findViewById(R.id.imageIC)
        var rNameOfItem: TextView =itemView.findViewById(R.id.textIC_Navn)
        var rColorOfFound: TextView =itemView.findViewById(R.id.textIC_Farge)
        var rDescriptionOfFound: TextView =itemView.findViewById(R.id.textIC_Besk)
        //var rContact: TextView =itemView.findViewById(R.id.te)
        //var rLat: TextView =itemView.findViewById(R.id.textIC_Navn)
        //var rLet: TextView =itemView.findViewById(R.id.textIC_Navn)
        var rTime: TextView =itemView.findViewById(R.id.textIC_dato)
        var rTypeOfPost: TextView =itemView.findViewById(R.id.textIC_Type)


    }




    private fun sortAlle(myAdapter: FirebaseRecyclerAdapter<Item, ItemViewHolder>) {
        displaList.clear()
        displaList.addAll(arrayList)
        myAdapter.notifyDataSetChanged()

    }

    private fun sortTapt(myAdapter: FirebaseRecyclerAdapter<Item, ItemViewHolder>) {
        displaList.clear()
        arrayList.forEach {
            if (it.typeOfPost == "Tapt")
                displaList.add(it)
        }
        myAdapter.notifyDataSetChanged()
    }

    private fun sortFunnet(myAdapter: FirebaseRecyclerAdapter<Item, ItemViewHolder>) {
        displaList.clear()
        arrayList.forEach {
            if (it.typeOfPost.contains("Funnet"))
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

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()) {
                        displaList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arrayList.forEach {
                            //Search definitions
                            if (it.nameOfItem.toLowerCase(Locale.getDefault())
                                    .contains(search) or (it.colorOfFound.toLowerCase(
                                    Locale.getDefault()
                                ).contains(search))
                            ) {
                                displaList.add(it)
                            }
                        }

                        hovedListe.adapter!!.notifyDataSetChanged()
                    } else {
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
           // sortDialog()
        }
        return super.onOptionsItemSelected(item)
    }




}