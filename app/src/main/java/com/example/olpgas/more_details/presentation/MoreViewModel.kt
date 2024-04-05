package com.example.olpgas.more_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.more_details.domain.use_case.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    fun onEvent(event: MoreEvent) {
        when(event) {
            MoreEvent.OnCreate -> TODO()
            MoreEvent.SignOut -> viewModelScope.launch { signOutUseCase() }
        }
    }
}