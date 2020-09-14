package com.example.lof_mob3000

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lof_mob3000.post_item.PostFoundItem
import kotlinx.android.synthetic.main.activity_post_found_item.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, PostFoundItem::class.java)
            startActivity(intent)
        }
    }

}