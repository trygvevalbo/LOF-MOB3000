package com.example.lof_mob3000

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.liste.*

class ItemViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_view)

        val actionBar : ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        var intent = intent
        val aNavn = intent.getStringExtra("iNavn")
        val aFarge = intent.getStringExtra("iFarge")
        val aBesk = intent.getStringExtra("iBesk")
        val aImage = intent.getIntExtra("iImage", 0)

        actionBar.setTitle(aNavn)
        textIC_Navn.text = aNavn
        textIC_Farge.text = aFarge
        textIC_Besk.text = aBesk
        imageIC.setImageResource(aImage)
    }
}