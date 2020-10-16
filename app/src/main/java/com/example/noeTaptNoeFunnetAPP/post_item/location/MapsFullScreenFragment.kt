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
import com.example.noeTaptNoeFunnetAPP.post_item.AppNavigator
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_description.view.*


class MapsFullScreenFragment : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest : LocationRequest
    private var itemLocation : LatLng? = null

    private lateinit var mMap: GoogleMap
    private var PERMISSION_ID  : Int= 1000

    private lateinit var appNavigator: AppNavigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_maps_full_screen, container, false)

        getLastLocation() // https://www.youtube.com/watch?v=vard0CUTLbA



        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(OnMapReadyCallback {
            //https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android/41254877
            mMap = it
            mMap.setOnMapClickListener { latLng -> // Creating a marker
                val markerOptions = MarkerOptions()
                itemLocation =latLng
                        // Setting the position for the marker
                markerOptions.position(latLng)

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

                // Clears the previously touched position
                mMap.clear()

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions)

            }
        })

        view.done_button.setOnClickListener {
            //itemLocation?.let { passData(it) }
            appNavigator.navigateFromMapToForm()
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appNavigator = context as AppNavigator

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
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    getNewLocation()

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
        override fun onLocationResult(p0: LocationResult){
            var lastLocation =p0.lastLocation
            val zoomLevel = 10
            val latlng = LatLng(lastLocation.latitude, lastLocation.longitude)
            val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
            mapFragment?.getMapAsync(OnMapReadyCallback {
                mMap = it

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoomLevel.toFloat()))
                })
        }
    }

}