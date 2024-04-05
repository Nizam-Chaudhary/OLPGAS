package com.example.olpgas.more_details.presentation

import com.example.olpgas.more_details.domain.util.ThemeMode

sealed class MoreEvent {
    data object SignOut: MoreEvent()
    data object OnCreate: MoreEvent()
    data class OnThemeModeChange(val themeMode: ThemeMode) : MoreEvent()
}