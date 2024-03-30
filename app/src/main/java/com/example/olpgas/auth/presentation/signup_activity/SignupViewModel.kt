package com.example.olpgas.auth.presentation.signup_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.domain.use_case.SignupUseCase
import com.example.olpgas.core.util.NetworkConnectivityObserver
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.domain.states.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel(){

    private val _userNameState:MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val userNameState: LiveData<TextFieldState> = _userNameState

    private val _emailState: MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val emailState: LiveData<TextFieldState> = _emailState

    private val _passwordState = MutableLiveData(TextFieldState())
    val passwordState: LiveData<TextFieldState> = _passwordState

    private val _confirmPasswordState = MutableLiveData(TextFieldState())
    val confirmPasswordState: LiveData<TextFieldState> = _confirmPasswordState

    private val _signupState = MutableLiveData<SignupState>()
    val signupState: LiveData<SignupState> = _signupState

    val connectionStatus = connectivityObserver.observe().asLiveData()

    fun onEvent(event: SignupEvent) {
        when(event) {
            is SignupEvent.EnteredUserName -> {
                _userNameState.value = userNameState.value?.copy(
                    text = event.userName
                )
            }
            is SignupEvent.EnteredEmail -> {
                _emailState.value = emailState.value?.copy(
                    text = event.email
                )
            }
            is SignupEvent.EnteredPassword -> {
                _passwordState.value = passwordState.value?.copy(
                    text = event.password
                )
            }
            is SignupEvent.EnteredConfirmPassword -> {
                _confirmPasswordState.value = confirmPasswordState.value?.copy(
                    text = event.confirmPassword
                )
            }
            SignupEvent.Signup -> {
                signup()
            }
        }
    }

    private fun signup() {
        viewModelScope.launch {
            _signupState.value = SignupState.Loading(true)
            _userNameState.value = userNameState.value?.copy(
                error = null
            )
            _emailState.value = emailState.value?.copy(
                error = null
            )
            _passwordState.value = passwordState.value?.copy(
                error = null
            )
            _confirmPasswordState.value = confirmPasswordState.value?.copy(
                error = null
            )
            val signupResult = signupUseCase(
                userNameState.value!!.text,
                emailState.value!!.text,
                passwordState.value!!.text,
                confirmPasswordState.value!!.text
            )

            if(signupResult.userNameError != null) {
                _userNameState.value = userNameState.value?.copy(
                    error = signupResult.userNameError
                )
            }

            if(signupResult.emailError != null) {
                _emailState.value = emailState.value?.copy(
                    error = signupResult.emailError
                )
            }

            if(signupResult.passwordError != null) {
                _passwordState.value = passwordState.value?.copy(
                    error = signupResult.passwordError
                )
            }

            if(signupResult.confirmPasswordError != null) {
                _confirmPasswordState.value = confirmPasswordState.value?.copy(
                    error = signupResult.confirmPasswordError
                )
            }

            when(signupResult.result) {
                is Resource.Success -> {
                    _signupState.value = SignupState.Success
                }
                is Resource.Error -> {
                    _signupState.value = SignupState.Error(signupResult.result.uiText)
                }
                null -> {

                }
            }
            _signupState.value = SignupState.Loading(false)
        }
    }
}