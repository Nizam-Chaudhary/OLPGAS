package com.example.olpgas.more_details.domain.use_case

data class MoreUseCases(
    val getUserNameUseCase: GetUserNameUseCase,
    val getUserProfileImageUseCase: GetUserProfileImageUseCase,
    val signOutUseCase: SignOutUseCase,
    val getThemeModeUseCase: GetThemeModeUseCase,
    val changeThemeModeUseCase: ChangeThemeModeUseCase
)
