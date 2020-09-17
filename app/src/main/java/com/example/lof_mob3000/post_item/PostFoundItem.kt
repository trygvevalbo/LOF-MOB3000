package com.example.lof_mob3000.post_item

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.lof_mob3000.R

class PostFoundItem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_found_item)

        val chooseButton = findViewById<Button>(R.id.chooseButton)
        chooseButton.setOnClickListener(chooseListener)

        chooseButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //https://stackoverflow.com/questions/44301301/android-how-to-achieve-setonclicklistener-in-kotlin
               new onShowUploadButton()
            }})

        fun onShowUploadButton(view: View){
            var uploadBtn = findViewById<Button>(R.id.uploadBtn)
            uploadBtn.isClickable = true
            uploadBtn.visibility = View.VISIBLE
        }
    }


}