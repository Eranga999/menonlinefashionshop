package com.example.madexam02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Home_screen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        // Button click listeners to navigate to Paymentgateway with respective prices and images
        val button190 = findViewById<Button>(R.id.button_190)
        button190.setOnClickListener {
            startPaymentGatewayActivity(190, R.drawable.men_clothes_3)
        }

        val button37 = findViewById<Button>(R.id.button_37)
        button37.setOnClickListener {
            startPaymentGatewayActivity(37, R.drawable.men11)
        }

        val button13 = findViewById<Button>(R.id.button_13)
        button13.setOnClickListener {
            startPaymentGatewayActivity(13, R.drawable.men12)
        }

        val button19 = findViewById<Button>(R.id.button_19)
        button19.setOnClickListener {
            startPaymentGatewayActivity(19, R.drawable.men_600_2)
        }

        val button27 = findViewById<Button>(R.id.button_27)
        button27.setOnClickListener {
            startPaymentGatewayActivity(27, R.drawable.men_600_3)
        }
    }

    private fun startPaymentGatewayActivity(price: Int, imageResId: Int) {
        val intent = Intent(this, Paymentgateway::class.java).apply {
            putExtra("EXTRA_PRICE", price)
            putExtra("EXTRA_IMAGE_RES_ID", imageResId)
        }
        startActivity(intent)
    }
}
