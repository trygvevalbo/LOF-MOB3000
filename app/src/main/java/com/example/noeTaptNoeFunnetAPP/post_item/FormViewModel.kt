package com.example.noeTaptNoeFunnetAPP.post_item

import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FormViewModel : ViewModel() {

    var postType: String? = null
    var userEmail = MutableLiveData<String>()
     var savedDescription = MutableLiveData<String>()
    var savedColor = MutableLiveData<String>()
    var savedLatitude = MutableLiveData<Double>()
    var savedLongitude = MutableLiveData<Double>()
    var savedTime = MutableLiveData<String>()
    var savedContact = MutableLiveData<String>()
     var image = MutableLiveData<Uri>()


    var savedNameItem = MutableLiveData<String>()


    @JvmName("setImage1")
    fun setImage(image: Uri?) {
        this.image.value = image
    }

    fun setNameItem(s: CharSequence, start: Int, before: Int, count: Int) {
        savedNameItem.value = s.toString()
    }

    fun setDescription(data: String) {
        savedDescription.value = data
    }

    fun setColor(s: CharSequence, start: Int, before: Int, count: Int) {
        savedColor.value = s.toString()
    }

    fun setLocation(latitude: Double, longitude: Double) {

        savedLatitude.value = latitude
         savedLongitude.value  = longitude
    }

    fun setContact(s: CharSequence, start: Int, before: Int, count: Int) {
        savedContact.value = s.toString()
    }


}