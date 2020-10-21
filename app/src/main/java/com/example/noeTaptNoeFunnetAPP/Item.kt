package com.example.noeTaptNoeFunnetAPP

class Item {

    var nameOfItem: String= " "
    var colorOfFound: String= " "
    var descriptionOfFound: String= " "
    var contact: String= " "
    var lat: String= " "
    var let: String= " "
    var time: String= " "
    var typeOfPost: String= " "
    var postImage: String =" "
    constructor(): this("","","","","","","","", "")
    constructor(
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
