package com.example.noeTaptNoeFunnetAPP.post_item

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R


class PostFoundItem : AppCompatActivity(), AppNavigator{
    private var descriptionText: String? = null
    private var viewModel: FormViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)

        viewModel = ViewModelProviders.of(this)[FormViewModel::class.java]




    }

    override fun navigateToDescription() {
        val action = FormFragmentDirections.actionFormFragmentToDescriptionFragment2(descriptionText)
        findNavController(R.id.nav_host_fragment).navigate(action)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
   override fun navigateToForm() {
       storeFormvalues()

             val action = DescriptionFragmentDirections.actionDescriptionFragmentToFormFragment(
                 descriptionText
             )
             findNavController(R.id.nav_host_fragment).navigate(action)

    }

    override fun navigateToSelectDate(){
        val action = FormFragmentDirections.actionFormFragmentToDateFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFromDateToForm(){
        val action = dateFragmentDirections.actionDateFragmentToFormFragment(descriptionText)
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateToMapFullScreen() {
        val action = FormFragmentDirections.actionFormFragmentToMapsFullScreenFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun storeFormvalues(){

        descriptionText?.let { viewModel?.setDescription(it) }
        val descriptionText = viewModel?.getDescription()

    }
    override fun onDataPass(data: String) {
        descriptionText = data

    }

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

    return true
    }*/


}