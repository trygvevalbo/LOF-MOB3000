package com.example.noeTaptNoeFunnetAPP.post_item.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.noeTaptNoeFunnetAPP.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.type.LatLng


class MapsFullScreenFragment : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    private var userLocation = ""

    private lateinit var mMap: GoogleMap
    private var PERMISSION_ID  : Int= 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_maps_full_screen, container, false)

        getLastLocation() // https://www.youtube.com/watch?v=vard0CUTLbA

        Toast.makeText(requireContext(), userLocation, Toast.LENGTH_LONG).show()

        val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
        return view

    }




    private fun checkPermission(): Boolean{
            if(
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED  ||
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                    ) {
                return true
            }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }


    private fun isLocationEnabeled(): Boolean{
        var locationManager =  context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode== PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug", "You have permission")
            }
        }
    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabeled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location = task.result
                    if(location == null){
                        getNewLocation()
                    }else{
                    if (location != null) {
                        userLocation = "din lokasjon er:\nLat:"+ location.latitude +"; Long:"+ location.longitude
                    }
                    }
                }
            }else{
                Toast.makeText(
                    requireContext(),
                    "vær så snill å godkjenn lokasjon",
                    Toast.LENGTH_LONG
                ).show()
            }
        }else{
            requestPermission()
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
        fun OnLocationResult(p0: LocationResult){
            var lastLocation =p0.lastLocation
            val zoomLevel = 10

            userLocation = "din lokasjon er:\nLat:"+ lastLocation.latitude +"; Long:"+ lastLocation.longitude
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, zoomLevel.toFloat()))

        }
    }

}