package com.example.olpgas.more_details.data.repository

import com.example.olpgas.more_details.data.local.SettingsSharedPreferences
import com.example.olpgas.more_details.domain.repository.MoreRepository

class MoreRepositoryImpl(
    private val settingsSharedPreferences: SettingsSharedPreferences
) : MoreRepository{
    override fun setThemeMode(themeMode: String) {
        settingsSharedPreferences.setThemeMode(themeMode)
    }

    override fun getThemeMode(): String? {
        return settingsSharedPreferences.getThemeMode()
    }
}