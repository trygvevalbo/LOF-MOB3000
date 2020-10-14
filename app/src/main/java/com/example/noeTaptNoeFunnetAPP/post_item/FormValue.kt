package com.example.noeTaptNoeFunnetAPP.post_item

class FormValue {
    var nameOfItem= ""
    var descriptionOfFound = ""
    var colorOfFound = ""

    var contact = ""
   var typeOfPost =""
    constructor(nameOfItem: String,descriptionOfFound:String, colorOfFound: String, contact: String,
                typeOfPost:String){
        this.nameOfItem = nameOfItem
        this.descriptionOfFound = descriptionOfFound
        this.colorOfFound = colorOfFound
        this.contact = contact
        this.typeOfPost= typeOfPost
    }
}