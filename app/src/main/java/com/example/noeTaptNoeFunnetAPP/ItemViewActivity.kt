package com.example.noeTaptNoeFunnetAPP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.liste.*

class ItemViewActivity : AppCompatActivity() , OnMapReadyCallback {
    lateinit var googleMap: GoogleMap
    private lateinit var mMap: GoogleMap
    private var itemLocation :  LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        var intent  = intent
        val aName   = intent.getStringExtra("iName")
        val aType   = intent.getStringExtra("iType")
        val aTime   = intent.getStringExtra("iTime")
        val aColor  = intent.getStringExtra("iColor")
        val aDesk   = intent.getStringExtra("iDesk")
        val aImage  = intent.getStringExtra("iImage")
        val aLat  = intent.getStringExtra("iLat")
        val aLng  = intent.getStringExtra("iLng")

        actionBar.title = aName
        textIC_Navn.text  = aName
        textIC_Type.text  = aType
        textIC_dato.text  = aTime
        textIC_Farge.text = aColor
        textIC_Besk.text  = aDesk
        Glide.with(this).load(aImage).into(imageIC)
        if (aLat != null && aLng != null) {
            itemLocation = LatLng(aLat.toDouble(), aLng.toDouble())
        }

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker to item found or lost
        // and move the map's camera to the same location.

        googleMap.addMarker(
            itemLocation?.let {
                MarkerOptions()
                    .position(it)
                    .title("Marker of item")
            }
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, 10f))

    }

}