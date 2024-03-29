package com.example.olpgas.user_profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.domain.use_case.UserProfileUsesCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileUsesCases: UserProfileUsesCases
): ViewModel() {

    private val _userProfileState = MutableLiveData(UserProfile())
    val userProfileState: LiveData<UserProfile> = _userProfileState

    private val _userProfilePictureState = MutableLiveData<ByteArray>()
    val userProfilePictureState: LiveData<ByteArray> = _userProfilePictureState

    fun onEvent(event: UserProfileEvent) {
        when(event) {
            UserProfileEvent.setUserProfile -> {
                viewModelScope.launch {
                    _userProfileState.value = userProfileUsesCases.getUserProfileUseCase()
                    _userProfilePictureState.value = userProfileUsesCases.getProfileImageUseCase()
                }
            }
            is UserProfileEvent.updateAge -> updateAge(event.age)
            is UserProfileEvent.updateGender -> updateGender(event.gender)
            is UserProfileEvent.updatePhoneNumber -> updatePhoneNumber(event.phoneNumber)
            is UserProfileEvent.updateAddress ->
                updateAddress(
                    event.streetNumber,
                    event.city,
                    event.state
                )
            is UserProfileEvent.updateProfileImage -> uploadProfileImage(event.imageByteArray)
        }
    }

    private fun updateAge(age: Int) {
        viewModelScope.launch {
            userProfileUsesCases.updateAgeUseCase(age)

            _userProfileState.value = userProfileState.value?.copy(
                age = age
            )
        }
    }

    private fun updateGender(gender: String) {
        viewModelScope.launch {
            userProfileUsesCases.updateGenderUseCase(gender)

            _userProfileState.value = userProfileState.value?.copy(
                gender = gender
            )
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch {
            userProfileUsesCases.updatePhoneNumberUseCase(phoneNumber)

            _userProfileState.value = userProfileState.value?.copy(
                phoneNumber = phoneNumber
            )
        }
    }

    private fun updateAddress(
        streetNumber: String,
        city: String,
        state: String,
    ) {
        viewModelScope.launch {
            userProfileUsesCases.updateAddressUseCase(
                streetNumber, city, state
            )

            _userProfileState.value = userProfileState.value?.copy(
                streetNumber = streetNumber,
                city = city,
                state = state
            )
        }
    }

    private fun uploadProfileImage(imageByteArray: ByteArray) {
        viewModelScope.launch {
            userProfileUsesCases.uploadProfileImageUseCase

            _userProfilePictureState.value = imageByteArray
        }
    }
}