package com.example.noeTaptNoeFunnetAPP.post_item

import android.text.Editable
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


    fun setNameItem(s: CharSequence, start: Int, before: Int, count: Int) {
        savedNameItem.value = s.toString()
    }

    fun setDescription(data: String) {
       savedDescription.setValue(data)
    }

    fun setColor(s: CharSequence, start: Int, before: Int, count: Int) {
        savedColor.value = s.toString()
    }

    fun setLocation(data: String) {
        savedLocation.value = data
    }

    fun setContact(s: CharSequence, start: Int, before: Int, count: Int) {
        savedContact.value = s.toString()
    }


}