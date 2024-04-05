package com.example.olpgas.more_details.data.local

import android.content.Context

class SettingsSharedPreferences(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(SETTINGS_SHARED_PREF, Context.MODE_PRIVATE)
    fun setThemeMode(themeMode: String) {
        sharedPreferences.edit()
            .putString(THEME_MODE_KEY, themeMode)
            .apply()
    }

    fun getThemeMode() : String? {
        return sharedPreferences.getString(THEME_MODE_KEY, "System")
    }

    companion object {
        const val SETTINGS_SHARED_PREF = "settings_shared_pref"

        const val THEME_MODE_KEY = "theme_mode"
    }
}