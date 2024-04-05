package com.example.olpgas.more_details.domain.use_case

import com.example.olpgas.more_details.domain.repository.MoreRepository

class ChangeThemeModeUseCase(
    private val moreRepository: MoreRepository
) {
    operator fun invoke(themeMode: String) {
        moreRepository.setThemeMode(themeMode)
    }
}