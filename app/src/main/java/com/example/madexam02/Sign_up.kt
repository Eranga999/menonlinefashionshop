package com.example.madexam02

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class Sign_up : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordToggle: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize UI components
        val createAccountButton = findViewById<Button>(R.id.createaccbutton)
        val loginButton = findViewById<Button>(R.id.login_button1)
        emailEditText = findViewById(R.id.editTextText)
        passwordEditText = findViewById(R.id.editTextTextPassword)
        passwordToggle = findViewById(R.id.passwordToggle)

        // Navigate to Create Account page
        createAccountButton.setOnClickListener {
            startActivity(Intent(this, Create_account::class.java))
        }

        // Login functionality
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateLogin(email, password)) {
                // Navigate to Home_screen after successful login
                startActivity(Intent(this, Home_screen::class.java))
                finish()
            } else {
                // Show error message if credentials don't match
                Toast.makeText(this, "Invalid login credentials. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        // Toggle password visibility
        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            passwordEditText.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_visibility_off)
        } else {
            passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_visibility)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun validateLogin(email: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPasswordHash = sharedPreferences.getString("password", null)

        // Check if email and password are not empty and match saved credentials
        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter both email and password.")
            return false
        }

        // Hash the entered password and compare with saved hashed password
        val inputPasswordHash = hashPassword(password)
        return email == savedEmail && inputPasswordHash == savedPasswordHash
    }

    private fun hashPassword(password: String): String {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val hashBytes = md.digest(password.toByteArray())
            val sb = StringBuilder()
            for (byte in hashBytes) {
                sb.append(String.format("%02x", byte))
            }
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            password // Fallback to plain password (not recommended)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
