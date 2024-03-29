package com.example.olpgas.user_profile.domain.use_case

data class UserProfileUsesCases(
    val addUserUseCase: AddUserUseCase,
    val getProfileImageUseCase: GetProfileImageUseCase,
    val getUserProfileUseCase: GetUserProfileUseCase,
    val updateAddressUseCase: UpdateAddressUseCase,
    val updateAgeUseCase: UpdateAgeUseCase,
    val updateGenderUseCase: UpdateGenderUseCase,
    val updatePhoneNumberUseCase: UpdatePhoneNumberUseCase,
    val uploadProfileImageUseCase: UploadProfileImageUseCase,
)
