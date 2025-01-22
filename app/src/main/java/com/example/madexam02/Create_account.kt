package com.example.madexam02

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Create_account : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)


        nameEditText = findViewById(R.id.editTextText2)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        phoneEditText = findViewById(R.id.editTextPhone)
        addressEditText = findViewById(R.id.editTextAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword2)
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            if (validateInput()) {
                saveUserData()
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, Sign_up::class.java))
                finish()
            }
        }
    }

    private fun validateInput(): Boolean {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        return when {
            name.isEmpty() -> {
                showToast("Name is required")
                false
            }
            email.isEmpty() -> {
                showToast("Email is required")
                false
            }
            !email.endsWith("@gmail.com") -> {
                showToast("Email must end with @gmail.com")
                false
            }
            phone.isEmpty() -> {
                showToast("Phone number is required")
                false
            }
            phone.length != 10 -> {
                showToast("Phone number must be 10 digits")
                false
            }
            address.isEmpty() -> {
                showToast("Address is required")
                false
            }
            password.isEmpty() -> {
                showToast("Password is required")
                false
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
                false
            }
            else -> true
        }
    }

    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", nameEditText.text.toString())
        editor.putString("email", emailEditText.text.toString())
        editor.putString("phone", phoneEditText.text.toString())
        editor.putString("address", addressEditText.text.toString())

        val hashedPassword = hashPassword(passwordEditText.text.toString())
        editor.putString("password", hashedPassword)
        editor.apply()
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
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            password
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
