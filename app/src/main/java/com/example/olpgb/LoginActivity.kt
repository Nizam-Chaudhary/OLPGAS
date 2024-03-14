package com.example.olpgb

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.olpgb.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Display System bar without resizing activity
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.btnLogin.setOnClickListener {
            val email = binding.edTxtEmail.text.toString()
            val password = binding.edTxtPassword.text.toString()

            var allValid = true

            if (!isValidEmail(email)) {
                binding.edTxtEmail.error = "Invalid email"
                allValid = false
            }

            if(password.isEmpty()) {
               binding.edTxtPassword.error = "Password cannot be empty"
            }

            if(allValid) {

            }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        binding.btnLoginGoogle.setOnClickListener {

        }
    }
}