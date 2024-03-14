package com.example.olpgb

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.olpgb.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Display System bar without resizing activity
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.edTxtEmail.text.toString()
            val password = binding.edTxtPassword.text.toString()
            val reTypedPassword = binding.edTxtConfirmPassword.text.toString()

            var allValid = true

            if(!isValidEmail(email)) {
                binding.edTxtEmail.error = "Invalid Email"
                allValid = false
            }

            if(password.length < 8) {
                binding.edTxtPassword.error = "must have 8 characters"
                allValid = false
            }

            if(password != reTypedPassword) {
                binding.edTxtConfirmPassword.error = "password does not match"
                allValid = false
            }

            if(allValid) {

            }
        }

        binding.btnLoginGoogle.setOnClickListener {

        }
    }
}