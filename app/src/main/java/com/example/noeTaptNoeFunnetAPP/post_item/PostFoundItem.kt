package com.example.noeTaptNoeFunnetAPP.post_item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R
import com.google.firebase.database.FirebaseDatabase



class PostFoundItem : AppCompatActivity(), AppNavigator{
    private var descriptionText: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)

        var database = FirebaseDatabase.getInstance().reference
        database.setValue("elvis")
    }

    override fun navigateToDescription() {
        val action = FormFragmentDirections.actionFormFragmentToDescriptionFragment2(descriptionText)
        findNavController(R.id.nav_host_fragment).navigate(action)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
   override fun navigateToForm() {

             val action = DescriptionFragmentDirections.actionDescriptionFragmentToFormFragment(
                 descriptionText
             )
             findNavController(R.id.nav_host_fragment).navigate(action)

    }

    override fun onDescriptionPass(data: String) {

        descriptionText = data

    }

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

    return true
    }*/


}