package com.example.noeTaptNoeFunnetAPP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_form.*
import kotlinx.android.synthetic.main.liste.*

class ItemViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        var intent  = intent
        val aName   = intent.getStringExtra("iName")
        val aType   = intent.getStringExtra("iType")
        val aTime   = intent.getStringExtra("iTime")
        val aColor  = intent.getStringExtra("iColor")
        val aDesk   = intent.getStringExtra("iDesk")
        val aImage  = intent.getStringExtra("iImage")

        actionBar.setTitle(aName)
        textIC_Navn.text  = aName
        textIC_Type.text  = aType
        textIC_dato.text  = aTime
        textIC_Farge.text = aColor
        textIC_Besk.text  = aDesk
        Glide.with(this).load(aImage).into(imageIC)
    }
}