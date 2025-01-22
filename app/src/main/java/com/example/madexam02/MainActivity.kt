package com.example.madexam02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val button =findViewById<Button>(R.id.buttn_1)
        button.setOnClickListener {
            val intent = Intent( this, onboard_2::class.java)
            startActivity(intent)
        }
    }
}