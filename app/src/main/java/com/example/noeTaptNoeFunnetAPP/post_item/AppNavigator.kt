package com.example.noeTaptNoeFunnetAPP.post_item

import android.text.Editable

interface AppNavigator {
    fun navigateToDescription()
    fun navigateToForm()
    fun navigateToSelectDate()
    fun navigateFromDateToForm()
    fun onDescriptionPass(data: String)

}