package com.example.olpgas.auth.domain.use_case

data class LoginUseCases(
    val loginUseCase: LoginUseCase,
    val googleSignInUseCase: GoogleSignInUseCase,
    val setUserProfileLocalCacheUseCase: SetUserProfileLocalCacheUseCase,
    val setUpUserUseCase: SetUpUserUseCase,
    val setUpUserWithGoogleUseCase: SetUpUserWithGoogleUseCase
)