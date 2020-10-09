package com.example.noeTaptNoeFunnetAPP.post_item

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noeTaptNoeFunnetAPP.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_maps, container, false)

      /*  mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            //googleMap.isMyLocationEnabled = true
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


        })*/


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync( OnMapReadyCallback{ googleMap = it
            //googleMap.isMyLocationEnabled = true
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


