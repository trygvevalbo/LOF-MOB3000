package com.example.noeTaptNoeFunnetAPP.post_item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.post_item.location.MapsFullScreenFragmentDirections
import com.google.android.gms.maps.model.LatLng


class PostFoundItem : AppCompatActivity(), AppNavigator{
    private var descriptionText: String? = null
    private var itemLocation : LatLng? = null
    private var viewModel: FormViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)

        viewModel = ViewModelProviders.of(this)[FormViewModel::class.java]




    }

    override fun navigateToDescription() {
        val action = FormFragmentDirections.actionFormFragmentToDescriptionFragment2()
        findNavController(R.id.nav_host_fragment).navigate(action)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
   override fun navigateToForm() {
      // storeFormvalues()

             val action = DescriptionFragmentDirections.actionDescriptionFragmentToFormFragment()
             findNavController(R.id.nav_host_fragment).navigate(action)

    }

    override fun navigateToSelectDate(){
        val action = FormFragmentDirections.actionFormFragmentToDateFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFromDateToForm(){
        val action = dateFragmentDirections.actionDateFragmentToFormFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateToMapFullScreen() {
        val action = FormFragmentDirections.actionFormFragmentToMapsFullScreenFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)
    }

    override fun navigateFromMapToForm(){
        val action = MapsFullScreenFragmentDirections.actionMapsFullScreenFragmentToFormFragment()
        findNavController(R.id.nav_host_fragment).navigate(action)

    }

    /*override fun storeFormvalues(){

        descriptionText?.let { viewModel?.setDescription(it) }
        val descriptionText = viewModel?.getDescription()

    }
    override fun onDataPass(data: String) {
        descriptionText = data

    }

    override fun onLocationPass(data: LatLng) {
        itemLocation = data
    }*/

    /*override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

    return true
    }*/


}