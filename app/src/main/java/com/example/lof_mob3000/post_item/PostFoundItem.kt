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




        fun onShowUploadButton(view: View){
            var uploadBtn = findViewById<Button>(R.id.uploadBtn)
            uploadBtn.isClickable = true
            uploadBtn.visibility = View.VISIBLE
        }
    }


}