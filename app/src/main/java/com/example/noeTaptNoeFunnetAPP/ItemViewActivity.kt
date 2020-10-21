package com.example.noeTaptNoeFunnetAPP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_item_view.*
import kotlinx.android.synthetic.main.liste.*
import kotlinx.android.synthetic.main.liste.textIC_Navn

class ItemViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        var intent  = intent
        val aNavn   = intent.getStringExtra("iNavn")
        val aType   = intent.getStringExtra("iType")
        val aDato   = intent.getStringExtra("iDato")
        val aFarge  = intent.getStringExtra("iFarge")
        val aBesk   = intent.getStringExtra("iBesk")
        val aImage  = intent.getIntExtra("iImage", 0)

        actionBar.setTitle(aNavn)
        textIW_Navn.text  = aNavn
        textIW_Type.text  = aType
        textIW_Time.text  = aDato
        textIW_Farge.text = aFarge
        textIW_Besk.text  = aBesk
        imageIC.setImageResource(aImage)
    }
}