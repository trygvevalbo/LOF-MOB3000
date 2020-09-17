package com.example.lof_mob3000.post_item

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.lof_mob3000.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*

//src https://www.youtube.com/watch?v=m_H3JuybsJ0
class MapsFragment : Fragment(), OnMapReadyCallback {

        private lateinit var googleMap: GoogleMap

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
           map_view.onCreate(savedInstanceState)
            map_view.onResume()
            map_view.getMapAsync(this)
        }

    override fun onMapReady(map: GoogleMap?) {
    map?.let{
        googleMap = it
    }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container,false)

    }
}
