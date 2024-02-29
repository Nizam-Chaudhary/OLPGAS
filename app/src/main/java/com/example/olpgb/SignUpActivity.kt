package com.example.olpgb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.olpgb.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    private val TAG = "Sign Up"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

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
                signUpUserWithEmailPassword(email, password)
            }
        }
    }

    private fun signUpUserWithEmailPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                } else {
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        binding.edTxtEmail.error = "Email already in use"
                    }
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Sign Up failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }
}