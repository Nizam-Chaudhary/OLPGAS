package com.example.olpgas.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.data.model.UserState
import com.example.olpgas.auth.data.network.SupabaseClient.client
import com.example.olpgas.auth.utils.SharedPreferenceHelper
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class SupabaseAuthViewModel : ViewModel() {

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState

    private val _isLoggedIn = MutableLiveData(true)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun signUp(
        context: Context,
        userName: String,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            try {
                _userState.value = UserState.Loading
                client.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Signed up successfully")
                client.postgrest.from("Users")
                    .upsert(mapOf("email" to userEmail, "userName" to userName))
            } catch(e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
                Log.e("Authentication", "Error: ${e.message}")
            }

        }
    }

    private fun saveToken(context: Context) {
        viewModelScope.launch {
            try {
                val accessToken = client.auth.currentSessionOrNull()?.accessToken
                if (accessToken != null) {
                    val sharedPreferencesHelper = SharedPreferenceHelper(context)
                    sharedPreferencesHelper.saveAccessToken(accessToken)
                    Log.i("Authentication", "Access token saved successfully")
                } else {
                    Log.w("Authentication", "No access token found in current session")
                }
            } catch (e: Exception) {
                Log.e("Authentication", "Error saving token: ${e.message}")
            }
        }
    }

    private fun getToken(context: Context): String? {
        return try {
            val sharedPreferencesHelper = SharedPreferenceHelper(context)
            sharedPreferencesHelper.getAccessToken()
        } catch (e: Exception) {
            Log.e("Supabase", "Error getting token from Keystore: ${e.message}")
            null
        }
    }

    fun login(
        context: Context,
        userEmail: String,
        userPassword: String,
    ) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                client.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = UserState.Success("Logged In Successfully")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
                Log.e("Authentication", "Error: ${e.message}")
            }

        }
    }

    fun logout(context: Context) {
        val sharedPreferencesHelper = SharedPreferenceHelper(context)
        viewModelScope.launch {
            try {
                client.auth.signOut()
                sharedPreferencesHelper.clearAccessToken()
                _userState.value = UserState.Success("Logged Out Successfully")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
                Log.e("Authentication", "Error: ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _isLoggedIn.value = false
                } else {
                    val session = client.auth.currentSessionOrNull()
                    if (session != null) {
                        _isLoggedIn.value = true
                    } else {
                        client.auth.refreshSession(token)
                        _isLoggedIn.value = true
                    }
                }
            } catch (e: Exception) {
                Log.e("Authentication", "Error: ${e.message}")
            }
        }
    }

    fun googleSignIn(context: Context) {
        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("716332734156-4fc1dnst6tgqg0m9q8j4lthl86bd94f0.apps.googleusercontent.com")
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context,
                    request
                )

                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                saveToken(context)

                client.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                _userState.value = UserState.Success("Signed in successfully")

                val currentUser = client.auth.currentUserOrNull()

                if (currentUser != null) {
                    val userName = currentUser.userMetadata?.get("name").toString()
                    val email = currentUser.email

                    client.postgrest.from("UserDetails")
                        .upsert(mapOf("email" to email, "userName" to userName.substring(1..userName.length-2)))
                }
            } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                _userState.value = UserState.Error("Error: ${e.message}")
                Log.e("Google Sign In", e.message.toString())
            } catch (e: GoogleIdTokenParsingException) {
                _userState.value = UserState.Error("Error: ${e.message}")
                Log.e("Google Sign In", e.message.toString())
            } catch (e: RestException) {
                _userState.value = UserState.Error("Error: ${e.message}")
                Log.e("Google Sign In", e.message.toString())
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error: ${e.message}")
                Log.e("Google Sign In", e.message.toString())
            }
        }
    }
}