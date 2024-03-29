package com.example.olpgas.auth.presentation.login_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.olpgas.auth.presentation.signup_activity.SignUpActivity
import com.example.olpgas.auth.presentation.util.AuthError
import com.example.olpgas.databinding.ActivityLoginBinding
import com.example.olpgas.roomdetails.ui.ViewRoomActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setState()

        onEmailTextChange()

        onPasswordTextChange()

        onSignupClick()

        onLoginClick()

        onGoogleSigninClick()

        observeLoginState()
    }

    private fun setState() {
        val emailState = viewModel.emailState.value
        val passwordState = viewModel.passwordState.value
        binding.txtFieldEmail.editText?.setText(emailState?.text)
        binding.txtFieldPassword.editText?.setText(passwordState?.text)
    }

    private fun onLoginClick() {
        binding.btnLogin.setOnClickListener {
            viewModel.onEvent(LoginEvent.Login)

            checkValidationError()
        }
    }

    private fun checkValidationError() {
        when(viewModel.emailState.value?.error) {
            AuthError.FieldEmpty -> binding.txtFieldEmail.error = "Email cannot be empty"
            AuthError.InValidEmail -> binding.txtFieldEmail.error = "Invalid email"
            else -> binding.txtFieldEmail.error = null
        }
        when(viewModel.passwordState.value?.error) {
            AuthError.FieldEmpty -> binding.txtFieldPassword.error = "Password cannot be empty"
            AuthError.InputTooShort -> binding.txtFieldPassword.error = "Password too short"
            else -> binding.txtFieldPassword.error = null
        }
    }

    private fun onSignupClick() {
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) {
            when(val loginState = viewModel.loginState.value) {
                is LoginState.Error -> {
                    binding.btnLogin.text = "Sign In"
                    Toast.makeText(this, loginState.message, Toast.LENGTH_SHORT).show()
                }
                LoginState.Loading -> {
                    binding.btnLogin.text = "Signing in..."
                }
                LoginState.Success -> {
                    binding.btnLogin.text = "Sign In"
                    startActivity(Intent(this, ViewRoomActivity::class.java))
                    finish()
                }
                null -> {
                    binding.btnLogin.text = "Sign In"
                }
            }
        }
    }

    private fun onEmailTextChange() {
        binding.txtFieldEmail.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldEmail.error = null
                viewModel.onEvent(LoginEvent.EnteredEmail(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun onPasswordTextChange() {
        binding.txtFieldPassword.editText?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.txtFieldPassword.error = null
                viewModel.onEvent(LoginEvent.EnteredPassword(s.toString()))
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun onGoogleSigninClick() {
        binding.btnLoginGoogle.setOnClickListener {
            val rawNonce = UUID.randomUUID().toString()
            CoroutineScope(Dispatchers.Main).launch {
                val googleIdToken = getGoogleIdToken(this@LoginActivity, rawNonce)
                if(googleIdToken != null) {
                    viewModel.onEvent(LoginEvent.GoogleSignIn(
                        rawNonce, googleIdToken
                    ))
                }
            }

        }
    }

    private suspend fun getGoogleIdToken(
        context: Context,
        rawNonce: String) : String? {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("716332734156-4fc1dnst6tgqg0m9q8j4lthl86bd94f0.apps.googleusercontent.com")
            .setNonce(getNonce(rawNonce))
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                context,
                request
            )

            val credential = result.credential

            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)

            return googleIdTokenCredential.idToken
        } catch (e: androidx.credentials.exceptions.GetCredentialException) {
            Log.e("Google Sign In", e.message.toString())
            return null
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("Google Sign In", e.message.toString())
            return null
        } catch (e: RestException) {
            Log.e("Google Sign In", e.message.toString())
            return null
        } catch (e: Exception) {
            Log.e("Google Sign In", e.message.toString())
            return null
        }

        /*Save User Details
        val currentUser = SupabaseClient.client.auth.currentUserOrNull()

        if (currentUser != null) {
                val userName = currentUser.userMetadata?.get("name").toString()
                val email = currentUser.email

                SupabaseClient.client.postgrest.from("UserDetails")
                    .upsert(mapOf("email" to email, "userName" to userName.substring(1..userName.length-2)))
        }*/
    }

    private fun getNonce(rawNonce: String) : String {
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}