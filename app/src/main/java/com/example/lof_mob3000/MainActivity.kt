package com.example.lof_mob3000

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lof_mob3000.post_item.PostFoundItem
import com.example.lof_mob3000.post_item.PostLostItem
import kotlinx.android.synthetic.main.activity_post_found_item.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            val intent = Intent(this, PostLostItem::class.java)
            startActivity(intent)
        }


        val button1 = findViewById<Button>(R.id.button1)
        button1.setOnClickListener{
            val intent1 = Intent(this, PostFoundItem::class.java)
            startActivity(intent1)
        }
    }

}