package com.example.olpgas.more_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.more_details.domain.use_case.MoreUseCases
import com.example.olpgas.more_details.domain.use_case.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val moreUseCases: MoreUseCases
) : ViewModel() {

    private val _userNameState = MutableLiveData<String>()
    val userNameState: LiveData<String> = _userNameState

    private val _userProfileImageState = MutableLiveData<ByteArray>()
    val userProfileImageState: LiveData<ByteArray> = _userProfileImageState

    fun onEvent(event: MoreEvent) {
        when(event) {
            MoreEvent.OnCreate -> {
                viewModelScope.launch {
                    _userNameState.value = moreUseCases.getUserNameUseCase()
                    _userProfileImageState.value = moreUseCases.getUserProfileImageUseCase()
                }
            }
            MoreEvent.SignOut -> viewModelScope.launch { moreUseCases.signOutUseCase() }
        }
    }
}