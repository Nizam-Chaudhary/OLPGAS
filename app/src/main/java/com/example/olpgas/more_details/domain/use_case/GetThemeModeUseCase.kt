package com.example.olpgas.more_details.domain.use_case

import com.example.olpgas.more_details.domain.repository.MoreRepository
import com.example.olpgas.more_details.domain.util.ThemeMode

class GetThemeModeUseCase (
    private val moreRepository: MoreRepository
) {
    operator fun invoke() : String {
        return moreRepository.getThemeMode() ?: ThemeMode.System.value
    }
}