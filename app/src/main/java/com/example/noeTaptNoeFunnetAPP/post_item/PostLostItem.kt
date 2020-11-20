package com.example.noeTaptNoeFunnetAPP.post_item

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.FrontPage
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.post_item.location.MapsFullScreenFragmentDirections
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_post_lost_item.*


class PostLostItem : AppCompatActivity(), AppNavigator {
    private var viewModel: FormViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_lost_item)

        viewModel = ViewModelProviders.of(this)[FormViewModel::class.java]
        viewModel!!.postType = "Tapt"

        val user = Firebase.auth.currentUser
        user?.let {
            for (profile in it.providerData) {

                viewModel!!.userEmail.value  = profile.email

            }
        }

        if(!intent.getStringExtra("iDocumentId").isNullOrEmpty()){
            viewModel!!.setPostData(intent)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Ny tapt annonse"
    }

    override fun navigateToDescription() {
        val action = FormFragmentDirections.actionFormFragmentToDescriptionFragment2()
        findNavController(R.id.nav_host_fragment).navigate(action)


    }
    override fun navigateToForm() {
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

}