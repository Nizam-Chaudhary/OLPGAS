package com.example.olpgas.main_activity.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.main_activity.domain.use_case.UserLoggedInStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userLoggedInStatus: UserLoggedInStatus
) : ViewModel() {

    private val _isUserLoggedInState = MutableLiveData(false)
    val isUserLoggedInState: LiveData<Boolean> = _isUserLoggedInState

    init {
        viewModelScope.launch { _isUserLoggedInState.value = userLoggedInStatus() }
    }
}