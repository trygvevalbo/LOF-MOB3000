package com.example.noeTaptNoeFunnetAPP.post_item

import android.text.Editable
import com.google.android.gms.maps.model.LatLng

interface AppNavigator {
    fun navigateToDescription()
    fun navigateToForm()
    fun navigateToSelectDate()
    fun navigateFromDateToForm()
    fun navigateToMapFullScreen()
    fun navigateFromMapToForm()
    fun onDataPass(data: String) //https://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity
    fun onLocationPass(data: LatLng)
    fun storeFormvalues()
}