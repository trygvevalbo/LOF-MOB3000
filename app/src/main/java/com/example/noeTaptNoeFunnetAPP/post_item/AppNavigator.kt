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

}