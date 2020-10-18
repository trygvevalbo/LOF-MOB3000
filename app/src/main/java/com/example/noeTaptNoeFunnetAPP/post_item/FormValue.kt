package com.example.noeTaptNoeFunnetAPP.post_item

class FormValue {
    var nameOfItem= ""
    var descriptionOfFound = ""
    var colorOfFound = ""
    var time=""
    var lat =""
    var lng=""
    var contact = ""
   var typeOfPost =""
    constructor(nameOfItem: String,descriptionOfFound:String, colorOfFound: String,time:String, lat: String,lng:String, contact: String,
                typeOfPost:String){
        this.nameOfItem = nameOfItem
        this.descriptionOfFound = descriptionOfFound
        this.colorOfFound = colorOfFound
        this.time= time
        this.lat=lat
        this.lng=lng
        this.contact = contact
        this.typeOfPost= typeOfPost
    }
}