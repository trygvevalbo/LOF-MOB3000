package com.example.noeTaptNoeFunnetAPP.post_item.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class LocationUtil(private val context: Context) {

    private var PERMISSION_ID  : Int= 1000

// Permission sjekk
    private fun checkPermission(): Boolean{
        if(
            ActivityCompat.checkSelfPermission(
                context ,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED  ||
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            context as Activity,
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


    fun getUserLocation(): Boolean{
        if(checkPermission()){
            if(isLocationEnabeled()){

                    return true


            }else{
                Toast.makeText(
                    context,
                    "vær så snill å godkjenn lokasjon",
                    Toast.LENGTH_LONG
                ).show()
            }
        }else{
            requestPermission()
            return true
        }
        return false
    }


}