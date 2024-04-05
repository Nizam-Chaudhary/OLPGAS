package com.example.olpgas.auth.presentation.signup_activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.olpgas.R
import com.example.olpgas.auth.presentation.login_activity.LoginActivity
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.Error
import com.example.olpgas.databinding.ActivitySignUpBinding
import com.example.olpgas.main_activity.presentation.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    val viewModel: SignupViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(binding.root)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            setState()

            onUserNameTextChange()

            onEmailTextChange()

            onPasswordTextChange()

            onConfirmPasswordTextChange()

            onSignupClick()

            onLoginClick()

            observeSignupState()

            observeConnection()
    }

    private fun setState() {
        val userNameState = viewModel.userNameState.value
        val emailState = viewModel.emailState.value
        val passwordState = viewModel.passwordState.value
        val confirmPasswordState = viewModel.confirmPasswordState.value
        binding.txtFieldUserName.editText?.setText(userNameState?.text)
        binding.txtFieldEmail.editText?.setText(emailState?.text)
        binding.txtFieldPassword.editText?.setText(passwordState?.text)
        binding.txtFieldConfirmPassword.editText?.setText(confirmPasswordState?.text)
    }

    private fun onUserNameTextChange() {
        binding.txtFieldUserName.editText?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldUserName.error = null
                viewModel.onEvent(SignupEvent.EnteredUserName(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onEmailTextChange() {
        binding.txtFieldEmail.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldEmail.error = null
                viewModel.onEvent(SignupEvent.EnteredEmail(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onPasswordTextChange() {
        binding.txtFieldPassword.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldPassword.error = null
                viewModel.onEvent(SignupEvent.EnteredPassword(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onConfirmPasswordTextChange() {
        binding.txtFieldConfirmPassword.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldConfirmPassword.error = null
                viewModel.onEvent(SignupEvent.EnteredConfirmPassword(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onSignupClick() {
        binding.btnSignup.setOnClickListener {
            if (viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                viewModel.onEvent(SignupEvent.Signup)

                checkValidationError()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Connection error")
                    .setMessage("Please check your network connection")
                    .setPositiveButton("dismiss") { _, _ -> }
                    .show()
            }
        }
    }

    private fun checkValidationError() {
        when(viewModel.userNameState.value?.error) {
            Error.FieldEmpty -> binding.txtFieldUserName.error = "User Name cannot be empty"
            else -> binding.txtFieldUserName.error = null
        }

        when(viewModel.emailState.value?.error) {
            Error.FieldEmpty -> binding.txtFieldEmail.error = "Email cannot be empty"
            Error.InValidEmail -> binding.txtFieldEmail.error = "Invalid email"
            else -> binding.txtFieldEmail.error = null
        }
        when(viewModel.passwordState.value?.error) {
            Error.FieldEmpty -> binding.txtFieldPassword.error = "Password cannot be empty"
            Error.InputTooShort -> binding.txtFieldPassword.error = "Password too short"
            else -> binding.txtFieldPassword.error = null
        }

        when(viewModel.confirmPasswordState.value?.error) {
            Error.PasswordsNotMatching -> binding.txtFieldConfirmPassword.error = "Password not matching"
            else -> binding.txtFieldConfirmPassword.error = null
        }
    }

    private fun onLoginClick() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_left, R.anim.slide_left)
            finish()
        }
    }

    private fun observeSignupState() {
        viewModel.signupState.observe(this){
            when(it) {
                is SignupState.Loading -> {
                    if(it.isLoading) {
                        binding.btnSignup.text = "Signing up"
                    } else {
                        binding.btnSignup.text = "Sign Up"
                    }
                }
                SignupState.Success -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    binding.btnSignup.text = "Sign Up"
                    finish()
                }
                is SignupState.Error -> {
                    binding.btnSignup.text = "Sign Up"
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeConnection() {
        viewModel.connectionStatus.observe(this) {
            Log.d("Network Connection", it.toString())
        }
    }
}