package com.example.noeTaptNoeFunnetAPP.post_item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.noeTaptNoeFunnetAPP.R
import kotlinx.android.synthetic.main.fragment_maps.*


class PostFoundItem : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)



        //supportFragmentManager
          //  .beginTransaction()
           // .add(R.id.form_container, FormFragment())
            //.add(R.id.maps_container, MapsFragment())
            //.commit()

       val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager


        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction


       transaction.add(R.id.root, FormFragment(), "Frag_Top_tag")
        transaction.add(R.id.maps_container, MapsFragment(), "Frag_Middle_tag")



        transaction.commit()


    }








}