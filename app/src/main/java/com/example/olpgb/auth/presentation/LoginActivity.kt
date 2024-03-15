package com.example.olpgb.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgb.MainActivity
import com.example.olpgb.auth.data.model.UserState
import com.example.olpgb.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgb.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val authViewModel: SupabaseAuthViewModel by lazy {
        ViewModelProvider(this)[SupabaseAuthViewModel::class.java]
    }

    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            authViewModel.userState.observe(this) {state ->
                when(state) {
                    UserState.Loading -> {
                        binding.btnLogin.text = "Logging In..."
                    }
                    is UserState.Success -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is UserState.Error -> {
                        binding.btnLogin.text = "Login"
                        Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Display System bar without resizing activity
            WindowCompat.setDecorFitsSystemWindows(window, false)

            binding.btnLogin.setOnClickListener {
                val email = binding.edTxtEmail.text.toString()
                val password = binding.edTxtPassword.text.toString()

                var allValid = true

                if (!isEmailValid(email)) {
                    binding.edTxtEmail.error = "Invalid email"
                    allValid = false
                }

                if(password.isEmpty()) {
                   binding.edTxtPassword.error = "Password cannot be empty"
                }

                if(allValid) {
                    authViewModel.login(this, email, password)
                }
            }

            binding.btnSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            binding.btnLoginGoogle.setOnClickListener {
            }
    }

    private fun isEmailValid(email: String) : Boolean {
        val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }
}