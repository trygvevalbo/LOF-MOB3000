package com.example.noeTaptNoeFunnetAPP.post_item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R


class PostFoundItem : AppCompatActivity(), AppNavigator{



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)
/*


       val manager: FragmentManager =
            supportFragmentManager //create an instance of fragment manager


        val transaction: FragmentTransaction =
            manager.beginTransaction() //create an instance of Fragment-transaction


       transaction.add(R.id.root, FormFragment(), "Frag_Top_tag")
        transaction.add(R.id.maps_container, MapsFragment(), "Frag_Middle_tag")

        transaction.commit()
*/


    }

    override fun navigateToDescription() {
        val action = FormFragmentDirections.actionFormFragmentToDescriptionFragment2()
        findNavController(R.id.nav_host_fragment).navigate(action)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun navigateToForm() {

        if(onSupportNavigateUp()== true) {
            val action = DescriptionFragmentDirections.actionDescriptionFragmentToFormFragment()
            findNavController(R.id.nav_host_fragment).navigate(action)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
    onBackPressed()
    return true
    }








}