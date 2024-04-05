package com.example.olpgas.more_details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.more_details.domain.use_case.MoreUseCases
import com.example.olpgas.more_details.domain.use_case.SignOutUseCase
import com.example.olpgas.more_details.domain.util.ThemeMode
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

    private val _themeModeState = MutableLiveData<String>()
    val themeModeState: LiveData<String> = _themeModeState

    fun onEvent(event: MoreEvent) {
        when(event) {
            MoreEvent.OnCreate -> {
                viewModelScope.launch {
                    _userNameState.value = moreUseCases.getUserNameUseCase()
                    _userProfileImageState.value = moreUseCases.getUserProfileImageUseCase()
                    _themeModeState.value = moreUseCases.getThemeModeUseCase()
                }
            }
            MoreEvent.SignOut -> viewModelScope.launch { moreUseCases.signOutUseCase() }
            is MoreEvent.OnThemeModeChange -> {
                viewModelScope.launch {
                    moreUseCases.changeThemeModeUseCase(event.themeMode.value)
                    _themeModeState.value = moreUseCases.getThemeModeUseCase()
                }
            }
        }
    }
}