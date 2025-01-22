package com.example.madexam02

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editTelephone: EditText
    private lateinit var editAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize UI components
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editTelephone = findViewById(R.id.editTelephone)
        editAddress = findViewById(R.id.editAddress)

        // Load existing user data into the EditTexts
        loadUserData()

        // Set click listener for the Save button
        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            if (validateInputs()) {
                saveUserData()
            }
        }
    }

    // Load user data from SharedPreferences into EditTexts
    private fun loadUserData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val telephone = sharedPreferences.getString("phone", "")  // Updated key to "phone"
        val address = sharedPreferences.getString("address", "")

        // Debugging logs
        Log.d("EditProfileActivity", "Loaded user data: Name=$name, Email=$email, Phone=$telephone, Address=$address")

        // Set the loaded data into the EditTexts
        editName.setText(name)
        editEmail.setText(email)
        editTelephone.setText(telephone)  // Correct key used here
        editAddress.setText(address)
    }

    // Validate user inputs
    private fun validateInputs(): Boolean {
        val name = editName.text.toString().trim()
        val email = editEmail.text.toString().trim()
        val telephone = editTelephone.text.toString().trim()
        val address = editAddress.text.toString().trim()

        return when {
            name.isEmpty() -> {
                showToast("Name cannot be empty")
                false
            }
            !email.endsWith("@gmail.com") -> {
                showToast("Email must end with '@gmail.com'")
                false
            }
            !isValidPhoneNumber(telephone) -> {
                showToast("Telephone must be exactly 10 digits")
                false
            }
            address.isEmpty() -> {
                showToast("Address cannot be empty")
                false
            }
            else -> true
        }
    }

    // Validate phone number format
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
    }

    // Show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Save the updated user data to SharedPreferences and return the result
    private fun saveUserData() {
        val name = editName.text.toString()
        val email = editEmail.text.toString()
        val telephone = editTelephone.text.toString()
        val address = editAddress.text.toString()

        // Save data to SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("phone", telephone)  // Updated key to "phone"
        editor.putString("address", address)
        editor.apply() // Save changes

        // Return updated data to Userprofile activity
        val resultIntent = Intent().apply {
            putExtra("name", name)
            putExtra("email", email)
            putExtra("phone", telephone)  // Use correct key here
            putExtra("address", address)
        }

        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
