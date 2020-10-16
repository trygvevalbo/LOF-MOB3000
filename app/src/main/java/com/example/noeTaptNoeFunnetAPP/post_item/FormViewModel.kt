package com.example.noeTaptNoeFunnetAPP.post_item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FormViewModel : ViewModel() {

    var savedNameItem = MutableLiveData<String>()
     var savedDescription = MutableLiveData<String>()
    var savedColor = MutableLiveData<String>()
    var savedLocation = MutableLiveData<String>()
    var savedContact = MutableLiveData<String>()

    fun setNameItem(desc: String) {
        savedDescription.setValue(desc)
    }

    fun setDescription(desc: String) {
       savedDescription.setValue(desc)
    }

    fun setColor(desc: String) {
        savedDescription.setValue(desc)
    }

    fun setLocation(desc: String) {
        savedDescription.setValue(desc)
    }

    fun setContact(desc: String) {
        savedDescription.setValue(desc)
    }

}