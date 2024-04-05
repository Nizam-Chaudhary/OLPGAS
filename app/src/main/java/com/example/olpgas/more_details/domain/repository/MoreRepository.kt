package com.example.olpgas.more_details.domain.repository

interface MoreRepository {
    fun setThemeMode(themeMode: String)

    fun getThemeMode() : String?
}