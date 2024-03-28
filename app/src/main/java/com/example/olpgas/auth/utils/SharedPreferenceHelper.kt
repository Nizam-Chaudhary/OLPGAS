package com.example.olpgas.auth.utils

import android.content.Context

class SharedPreferenceHelper(private val context: Context) {

    companion object {
        private const val MY_PREF_KEY = "PREF_KEY"
        private const val MY_ACCESS_TOKEN = "ACCESS_TOKEN"
    }

    fun saveAccessToken(accessToken: String?) {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(MY_ACCESS_TOKEN, accessToken).apply()
    }

    fun getAccessToken() : String? {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(MY_ACCESS_TOKEN, null)
    }

    fun clearAccessToken() {
        val sharedPreferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.edit().clear().apply()

    }
}