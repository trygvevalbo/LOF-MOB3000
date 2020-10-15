package com.example.noeTaptNoeFunnetAPP.post_item

import android.text.Editable

interface AppNavigator {
    fun navigateToDescription()
    fun navigateToForm()
    fun navigateToSelectDate()
    fun navigateFromDateToForm()
    fun navigateToMapFullScreen()
    fun onDataPass(data: String) //https://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity
    fun storeFormvalues()
}