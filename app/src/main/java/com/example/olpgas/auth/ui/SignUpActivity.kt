package com.example.olpgas.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.olpgas.auth.data.model.UserState
import com.example.olpgas.auth.viewmodel.SupabaseAuthViewModel
import com.example.olpgas.databinding.ActivitySignUpBinding
import com.example.olpgas.roomdetails.ui.ViewRoomActivity

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
                        val intent = Intent(this, ViewRoomActivity::class.java)
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
                val userName = binding.txtFieldUserName.editText?.text.toString()
                val email = binding.txtFieldEMail.editText?.text.toString()
                val password = binding.txtFieldPassword.editText?.text.toString()
                val reTypedPassword = binding.txtFieldConfirmPassword.editText?.text.toString()

                var allValid = true

                if(userName.isEmpty()) {
                    binding.txtFieldUserName.error = "Name cannot be empty"
                }

                if(!isEmailValid(email)) {
                    binding.txtFieldEMail.error = "Invalid Email"
                    allValid = false
                }

                if(!isPasswordValid(password)) {
                    binding.txtFieldPassword.error = "must have 8 characters"
                    allValid = false
                }

                if(password != reTypedPassword) {
                    binding.txtFieldConfirmPassword.error = "password does not match"
                    allValid = false
                }

                if(allValid) {
                    authViewModel.signUp(this, userName, email, password)
                }
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