package com.example.noeTaptNoeFunnetAPP.post_item

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FormViewModel : ViewModel() {


     var savedDescription = MutableLiveData<String>()
    var savedColor = MutableLiveData<String>()
    var savedLocation = MutableLiveData<String>()
    var savedTime = MutableLiveData<String>()
    var savedContact = MutableLiveData<String>()


    var savedNameItem = MutableLiveData<String>()


    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        savedNameItem.setValue(s.toString())

    }



    fun setNameItem(data: String) {
        savedNameItem.setValue(data)
    }

    fun setDescription(data: String) {
       savedDescription.setValue(data)
    }

    fun setColor(data: String) {
        savedColor.setValue(data)
    }

    fun setLocation(data: String) {
        savedLocation.setValue(data)
    }

    fun setContact(data: String) {
        savedContact.setValue(data)
    }


}