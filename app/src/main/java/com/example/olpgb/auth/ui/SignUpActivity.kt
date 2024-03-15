package com.example.olpgb.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgb.auth.data.model.UserState
import com.example.olpgb.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgb.databinding.ActivitySignUpBinding
import com.example.olpgb.roomdetails.ui.MainActivity

class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private val authViewModel: SupabaseAuthViewModel by lazy {
        ViewModelProvider(this)[SupabaseAuthViewModel::class.java]
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(binding.root)

            // Display System bar without resizing activity
            WindowCompat.setDecorFitsSystemWindows(window, false)

            authViewModel.userState.observe(this) {state ->
                when(state) {
                    UserState.Loading -> binding.btnSignup.text = "Signing Up..."
                    is UserState.Success -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is UserState.Error -> {
                        binding.btnSignup.text = "SignUp"
                        Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.btnLogin.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }

            binding.btnSignup.setOnClickListener {
                val email = binding.edTxtEmail.text.toString()
                val password = binding.edTxtPassword.text.toString()
                val reTypedPassword = binding.edTxtConfirmPassword.text.toString()

                var allValid = true

                if(!isEmailValid(email)) {
                    binding.edTxtEmail.error = "Invalid Email"
                    allValid = false
                }

                if(!isPasswordValid(password)) {
                    binding.edTxtPassword.error = "must have 8 characters"
                    allValid = false
                }

                if(password != reTypedPassword) {
                    binding.edTxtConfirmPassword.error = "password does not match"
                    allValid = false
                }

                if(allValid) {
                    authViewModel.signUp(this, email, password)
                }
            }

            binding.btnLoginGoogle.setOnClickListener {
        }
    }

    private fun isEmailValid(email: String) : Boolean {
        val emailRegex = "^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
        return emailRegex.matches(email)
    }

    private fun isPasswordValid(password: String) : Boolean {
        return password.length >= 8
    }
}