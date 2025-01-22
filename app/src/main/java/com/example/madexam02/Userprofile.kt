package com.example.madexam02

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class Userprofile : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var telephoneTextView: TextView
    private lateinit var addressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userprofile)

        // Initialize TextViews
        nameTextView = findViewById(R.id.name)
        emailTextView = findViewById(R.id.mail)
        telephoneTextView = findViewById(R.id.tele)
        addressTextView = findViewById(R.id.address)

        // Load user data
        loadUserData()

        // Edit button click listener
        val editButton = findViewById<Button>(R.id.editProfileButton)
        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }

        // Logout button click listener
        val logoutButton = findViewById<Button>(R.id.button_000)
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    // Load user data from SharedPreferences
    private fun loadUserData() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "No Name")
        val email = sharedPreferences.getString("email", "No Email")
        val phone = sharedPreferences.getString("phone", "No Phone")  // Key changed to "phone"
        val address = sharedPreferences.getString("address", "No Address")

        // Set user data in TextViews
        nameTextView.text = "Name: $name"
        emailTextView.text = "Email: $email"
        telephoneTextView.text = "Phone: $phone"  // Key corrected here
        addressTextView.text = "Address: $address"
    }

    // Handle result from EditProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            // Reload user data after profile edit
            loadUserData()
        }
    }

    // Show logout confirmation dialog
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ -> logout() }
            .setNegativeButton("No", null)
            .show()
    }

    // Clear SharedPreferences and redirect to Sign_up screen
    private fun logout() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()  // Clear user data

        // Redirect to Sign_up screen
        val intent = Intent(this, Sign_up::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }

    companion object {
        private const val REQUEST_CODE_EDIT = 1
    }
}
