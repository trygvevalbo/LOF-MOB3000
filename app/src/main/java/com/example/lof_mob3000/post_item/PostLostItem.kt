package com.example.lof_mob3000.post_item

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lof_mob3000.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PostLostItem : AppCompatActivity() {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap:GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_lost_item)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
          //  googleMap.isMyLocationEnabled = true
            val location1 = LatLng(62.479386, 6.819220)
            googleMap.addMarker(MarkerOptions().position(location1).title("My location"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location1, 10f))

                //https://stackoverflow.com/questions/41254834/add-marker-on-google-map-on-touching-the-screen-using-android/41254877
            googleMap.setOnMapClickListener { latLng -> // Creating a marker
                val markerOptions = MarkerOptions()

                // Setting the position for the marker
                markerOptions.position(latLng)

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

                // Clears the previously touched position
                googleMap.clear()

                // Animating to the touched position
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

                // Placing a marker on the touched position
                googleMap.addMarker(markerOptions)
            }


        })



    }
}