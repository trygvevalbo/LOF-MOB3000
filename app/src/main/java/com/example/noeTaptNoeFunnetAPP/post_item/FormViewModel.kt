package com.example.noeTaptNoeFunnetAPP.post_item

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


class FormViewModel : ViewModel() {

    var documentId : String? = null
    var postType: String? = null
    var userEmail = MutableLiveData<String>()
     var savedDescription = MutableLiveData<String>()
    var savedColor = MutableLiveData<String>()
    var savedLatitude = MutableLiveData<Double>()
    var savedLongitude = MutableLiveData<Double>()
    var userLatitude : Double?= null
    var userLongitude : Double?= null
    var savedTime = MutableLiveData<String>()
    var savedContact = MutableLiveData<String>()
     var image = MutableLiveData<Uri>()
    var imageUrl :String? = null
    var downloadUrl: String? = null


    var savedNameItem = MutableLiveData<String>()


    @JvmName("setImage1")
    fun setImage(image: Uri?) {
        this.image.value = image!!
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

    fun setUserLocation(latitude: Double, longitude: Double) {

        userLatitude = latitude
        userLongitude  = longitude
    }

    fun setContact(s: CharSequence, start: Int, before: Int, count: Int) {
        savedContact.value = s.toString()
    }

    fun setPostData(intent: Intent) {

        val aDocumentId = intent.getStringExtra("iDocumentId")
        val aName   = intent.getStringExtra("iName")
        val aType   = intent.getStringExtra("iType")
        val aTime   = intent.getStringExtra("iTime")
        val aColor  = intent.getStringExtra("iColor")
        val aDesk   = intent.getStringExtra("iDesk")
        val aImage  = intent.getStringExtra("iImage")
        val aContact = intent.getStringExtra("iContact")
        val aLat  = intent.getStringExtra("iLat")
        val aLng  = intent.getStringExtra("iLng")
        val aEmail = intent.getStringExtra("iEmail")

        documentId = aDocumentId
        savedNameItem.value = aName!!
        postType = aType
         userEmail.value = aEmail!!
         savedDescription.value = aDesk!!
         savedColor.value = aColor!!
        if (aLat != null) {
            savedLatitude.value = aLat.toDouble()
        }
        if (aLng != null) {
            savedLongitude.value = aLng.toDouble()
        }
         savedTime.value =aTime!!
         savedContact.value = aContact!!


            downloadUrl=aImage
            //image.value =  Uri.parse(aImage)


    }

}