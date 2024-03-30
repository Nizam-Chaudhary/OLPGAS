package com.example.olpgas.auth.data.remote

import android.content.SharedPreferences
import android.util.Log
import com.example.olpgas.core.data.remote.SupabaseClient
import com.example.olpgas.core.util.Constants
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.SimpleResource
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken

class SupabaseAuth(
    private val authSharedPreferences: SharedPreferences
) {
    companion object {
        private const val TAG = "Supabase Auth"
    }
    suspend fun login(
        userEmail: String,
        userPassword: String,
    ) : SimpleResource {
        return try{
            SupabaseClient.client.auth.signInWith(Email) {
                email = userEmail
                password = userPassword
            }

            val accessToken = SupabaseClient.client.auth.currentSessionOrNull()?.accessToken
            val userId = SupabaseClient.client.auth.currentUserOrNull()?.id
            authSharedPreferences.edit()
                .putString(Constants.ACCESS_TOKEN, accessToken)
                .putString(Constants.USER_ID, userId)
                .apply()

            Resource.Success(Unit)
        } catch (e: BadRequestRestException) {
            Log.e(TAG, "Error: ${e.message}")
            Resource.Error("Invalid Login Credentials")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            Resource.Error("Error Logging in")
        }
    }

    suspend fun signUp(
        userEmail: String,
        userPassword: String,
    ) : SimpleResource {
        return try{
            SupabaseClient.client.auth.signUpWith(Email) {
                email = userEmail
                password = userPassword
            }

            val accessToken = SupabaseClient.client.auth.currentSessionOrNull()?.accessToken
            val userId = SupabaseClient.client.auth.currentUserOrNull()?.id
            authSharedPreferences.edit()
                .putString(Constants.ACCESS_TOKEN, accessToken)
                .putString(Constants.USER_ID, userId)
                .apply()

            Resource.Success(Unit)
        } catch (e: BadRequestRestException) {
            Log.e(TAG, "Error: ${e.message}")
            Resource.Error("Email already registered")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            Resource.Error("Error Logging in")
        }
    }

    suspend fun logout() : SimpleResource {
        return try{
            SupabaseClient.client.auth.signOut()
            authSharedPreferences.edit().clear().apply()
            Resource.Success(Unit)
        }catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            Resource.Error("Error Logging out")
        }
    }

    suspend fun signInWithGoogle(
        googleIdToken: String,
        rawNonce: String
    ) : SimpleResource {
        return try{
            SupabaseClient.client.auth.signInWith(IDToken) {
                idToken = googleIdToken
                provider = Google
                nonce = rawNonce
            }

            val accessToken = SupabaseClient.client.auth.currentSessionOrNull()?.accessToken
            val userId = SupabaseClient.client.auth.currentUserOrNull()?.id
            authSharedPreferences.edit()
                .putString(Constants.ACCESS_TOKEN, accessToken)
                .putString(Constants.USER_ID, userId)
                .apply()

            Resource.Success(Unit)
        }catch (e: Exception) {
            Resource.Error("Error Signing in with Google")
        }
    }

    fun isUserLoggedIn() : Boolean {
        authSharedPreferences.getString(Constants.ACCESS_TOKEN, null) ?: return false
        if(SupabaseClient.client.auth.currentSessionOrNull() != null) {
            return true
        }
        return true
    }
}