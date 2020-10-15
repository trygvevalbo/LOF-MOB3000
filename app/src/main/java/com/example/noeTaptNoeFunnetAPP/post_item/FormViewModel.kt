package com.example.noeTaptNoeFunnetAPP.post_item

import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {
    private var savedDescription : String =""


    fun getDescription(): String? {
        return savedDescription
    }

    fun setDescription(savedDescription: String) {
        this.savedDescription = savedDescription
    }

}