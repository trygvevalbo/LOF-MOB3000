package com.example.noeTaptNoeFunnetAPP.post_item.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.noeTaptNoeFunnetAPP.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_maps_full_screen.*




class MapsFullScreenFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private var ACCESS_LOCATION_REQUEST_CODE  : Int= 1001


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_maps_full_screen, container, false)

       if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)  //https://www.youtube.com/watch?v=mBOCAHsGkzs
           == PackageManager.PERMISSION_GRANTED){
        enableUserLocation()
       }else{
           if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
               ActivityCompat.requestPermissions(requireActivity(), String[]{Manifest.permission.ACCESS_FINE_LOCATION},
               ACCESS_LOCATION_REQUEST_CODE)
           } else {
               ActivityCompat.requestPermissions(requireActivity(), String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                   ACCESS_LOCATION_REQUEST_CODE)
           }
       }

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun enableUserLocation(){

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == ACCESS_LOCATION_REQUEST_CODE){
            
        }
    }

}