package com.example.madexam02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class onboard_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboard2)

        val button =findViewById<Button>(R.id.button_3)
        button.setOnClickListener {
            val intent = Intent(this, Sign_up::class.java)
            startActivity(intent)
        }

    }
}