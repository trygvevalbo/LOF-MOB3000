package com.example.noeTaptNoeFunnetAPP.post_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.noeTaptNoeFunnetAPP.R
import com.example.noeTaptNoeFunnetAPP.post_item.location.LocationUtil
import com.example.noeTaptNoeFunnetAPP.post_item.location.MapsFullScreenFragmentDirections
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PostFoundItem : AppCompatActivity(), AppNavigator{

    private var viewModel: FormViewModel? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item) //setter sin egen layout og derretter formFragment i første omgang

        viewModel = ViewModelProviders.of(this)[FormViewModel::class.java]
        viewModel!!.postType = "Funnet"

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
        supportActionBar?.title = "Ny funnet annonse"

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
            val zoomLevel = 10
            val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)


            viewModel?.setUserLocation(lastLocation.latitude, lastLocation.longitude)

        }
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