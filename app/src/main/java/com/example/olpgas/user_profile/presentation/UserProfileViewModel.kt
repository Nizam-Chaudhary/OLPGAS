package com.example.olpgas.user_profile.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.NetworkConnectivityObserver
import com.example.olpgas.user_profile.data.model.UserProfile
import com.example.olpgas.user_profile.domain.use_case.UserProfileUsesCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileUsesCases: UserProfileUsesCases,
    private val networkConnectivityObserver: ConnectivityObserver
): ViewModel() {

    private val _userProfileState = MutableLiveData(UserProfile())
    val userProfileState: LiveData<UserProfile> = _userProfileState

    private val _userProfilePictureState = MutableLiveData<ByteArray>()
    val userProfilePictureState: LiveData<ByteArray> = _userProfilePictureState

    val connectionStatus = networkConnectivityObserver.observe().asLiveData()

    fun onEvent(event: UserProfileEvent) {
        when(event) {
            UserProfileEvent.SetUserProfile -> {
                viewModelScope.launch {
                    _userProfileState.value = userProfileUsesCases.getUserProfileUseCase()
                    _userProfilePictureState.value = userProfileUsesCases.getProfileImageUseCase()
                }
            }
            is UserProfileEvent.UpdateAge -> updateAge(event.age)
            is UserProfileEvent.UpdateGender -> updateGender(event.gender)
            is UserProfileEvent.UpdatePhoneNumber -> updatePhoneNumber(event.phoneNumber)
            is UserProfileEvent.UpdateAddress ->
                updateAddress(
                    event.streetNumber,
                    event.city,
                    event.state
                )
            is UserProfileEvent.UpdateProfileImage -> uploadProfileImage(event.imageByteArray)
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
            userProfileUsesCases.uploadProfileImageUseCase(imageByteArray)

            _userProfilePictureState.value = userProfileUsesCases.getProfileImageUseCase()
        }
    }
}