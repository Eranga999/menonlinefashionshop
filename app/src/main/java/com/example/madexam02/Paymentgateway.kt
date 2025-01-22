package com.example.madexam02

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Vibrator
import android.os.VibrationEffect
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Paymentgateway : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var countdownTextView: TextView
    private lateinit var progressDialog: androidx.appcompat.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paymentgateway)

        createNotificationChannel() // Create the notification channel

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("PaymentPrefs", Context.MODE_PRIVATE)

        // Retrieve the item data from the intent
        val price = intent.getIntExtra("EXTRA_PRICE", 0)
        val imageResId = intent.getIntExtra("EXTRA_IMAGE_RES_ID", R.drawable.default_image) // Replace with a default image if needed

        // Initialize UI elements
        val cardImageView = findViewById<ImageView>(R.id.cardImageView)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val payNowButton = findViewById<Button>(R.id.payNowButton)
       // Ensure you have this TextView in your layout

        // Set the data to the UI elements
        cardImageView.setImageResource(imageResId)
        priceTextView.text = "Price: $$price"

        // Set up the back button to go to the previous screen
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Set up the Pay Now button to handle payment
        payNowButton.setOnClickListener {
            handlePayment()
        }

        // Apply edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            view.setPadding(
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            )
            insets
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "payment_channel"
            val channelName = "Payment Notifications"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Channel for payment notifications"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val intent = Intent(this, Paymentgateway::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, "payment_channel")
            .setSmallIcon(R.drawable.ic_payment) // Replace with your notification icon
            .setContentTitle("Payment Status")
            .setContentText("Payment was successful!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    private fun handlePayment() {
        val nameOnCard = findViewById<EditText>(R.id.userNameEditText).text.toString()
        val cardNumber = findViewById<EditText>(R.id.cardNumberEditText).text.toString()
        val expiryDate = findViewById<EditText>(R.id.expiryDateEditText).text.toString()
        val cvv = findViewById<EditText>(R.id.cvvEditText).text.toString()

        // Validate inputs
        if (nameOnCard.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate expiry date (MM/YY format)
        if (!expiryDate.matches(Regex("\\d{2}/\\d{2}"))) {
            Toast.makeText(this, "Expiry date must be in MM/YY format", Toast.LENGTH_SHORT).show()
            return
        }

        // Save payment details to SharedPreferences
        with(sharedPreferences.edit()) {
            putString("NameOnCard", nameOnCard)
            putString("CardNumber", cardNumber)
            putString("ExpiryDate", expiryDate)
            putString("CVV", cvv)
            apply()
        }

        Toast.makeText(this, "Payment details saved", Toast.LENGTH_SHORT).show()

        // Show progress dialog and simulate payment process
        showProgressDialogAndSimulatePayment()
    }

    private fun showProgressDialogAndSimulatePayment() {
        // Show a progress dialog
        progressDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Processing Payment")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create()

        progressDialog.show()

        // Set up countdown timer
        val countdownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                countdownTextView.text = "Time remaining: $secondsRemaining seconds"
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                // Dismiss the progress dialog
                progressDialog.dismiss()

                // Show notification
                sendNotification()

                // Vibrate the phone
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (vibrator.hasVibrator()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                }

                // Show success message
                Toast.makeText(this@Paymentgateway, "Payment Successful!", Toast.LENGTH_LONG).show()
            }
        }.start()
    }
}
