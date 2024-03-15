package com.example.olpgas.auth.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class SupabaseAuthViewModel : ViewModel() {

    private val _userState = MutableLiveData<UserState>()
    val userState: LiveData<UserState> = _userState

    private val _isLoggedIn = MutableLiveData<Boolean>(true)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun signUp(
        context: Context,
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
            } catch(e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
            }

        }
    }

    private fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = client.auth.currentAccessTokenOrNull()
            val sharedPref = SharedPreferenceHelper(context)
            sharedPref.saveStringData("accessToken",accessToken)
        }

    }

    private fun getToken(context: Context): String? {
        val sharedPref = SharedPreferenceHelper(context)
        return sharedPref.getStringData("accessToken")
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
            }

        }
    }

    fun logout(context: Context) {
        val sharedPref = SharedPreferenceHelper(context)
        viewModelScope.launch {
            try {
                client.auth.signOut()
                sharedPref.clearPreferences()
                _userState.value = UserState.Success("Logged Out Successfully")
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
            }
        }
    }

    fun isUserLoggedIn(
        context: Context,
    ) {
        viewModelScope.launch {
            try {
                val token = getToken(context)
                if(token.isNullOrEmpty()) {
                    _isLoggedIn.value = false
                } else {
                    client.auth.retrieveUser(token)
                    client.auth.refreshCurrentSession()
                    saveToken(context)
                    _userState.value = UserState.Success("Already Logged In")
                    _isLoggedIn.value = true
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error("Error : ${e.message}")
            }
        }
    }
}