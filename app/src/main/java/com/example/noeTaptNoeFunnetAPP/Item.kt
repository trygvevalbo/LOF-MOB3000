package com.example.noeTaptNoeFunnetAPP

import android.icu.text.UnicodeSet.EMPTY

class Item {
    var imageUrl: String = " "
    var nameOfItem: String= " "
    var colorOfFound: String= " "
    var descriptionOfFound: String= " "
    var contact: String= " "
    var lat: String= " "
    var let: String= " "
    var time: String= " "
    var typeOfPost: String= " "
    var postImage: String= ""
    constructor(): this("","","","","","","","", "")
    constructor(
        imageUrl: String,
        nameOfItem: String,
        colorOfFound: String,
        descriptionOfFound: String,
        contact: String,
        lat: String,
        let: String,
        time: String,
        typeOfPost: String,
        postImage: String
    ) {
        this.imageUrl=imageUrl
        this.nameOfItem = nameOfItem
        this.colorOfFound = colorOfFound
        this.descriptionOfFound = descriptionOfFound
        this.contact = contact
        this.lat = lat
        this.let = let
        this.time = time
        this.typeOfPost = typeOfPost
        this.postImage = postImage
    }


}
