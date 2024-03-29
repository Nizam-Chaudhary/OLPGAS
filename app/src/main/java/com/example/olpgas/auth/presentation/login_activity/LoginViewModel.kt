package com.example.olpgas.auth.presentation.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.domain.use_case.GoogleSignInUseCase
import com.example.olpgas.auth.domain.use_case.LoginUseCase
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.domain.states.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _emailState:MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val emailState: LiveData<TextFieldState> = _emailState

    private val _passwordState = MutableLiveData(TextFieldState())
    val passwordState: LiveData<TextFieldState> = _passwordState

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.EnteredEmail -> {
                _emailState.value = emailState.value?.copy(
                    text = event.email
                )
            }
            is LoginEvent.EnteredPassword -> {
                _passwordState.value = passwordState.value?.copy(
                    text = event.password
                )
            }
            is LoginEvent.GoogleSignIn -> {
                googleSignIn(event.rawNonce, event.googleIdToken)
            }
            LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            _emailState.value = emailState.value?.copy(
                error = null
            )
            _passwordState.value = passwordState.value?.copy(
                error = null
            )

            val loginResult = loginUseCase(
                emailState.value!!.text,
                passwordState.value!!.text
            )

            if(loginResult.emailError != null) {
                _emailState.value = emailState.value?.copy(
                    error = loginResult.emailError
                )
            }

            if(loginResult.passwordError != null) {
                _passwordState.value = passwordState.value?.copy(
                    error = loginResult.passwordError
                )
            }

            when(loginResult.result) {
                is Resource.Success -> {
                    _loginState.value = LoginState.Success
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(loginResult.result.uiText)
                }
                null -> {}
            }
        }
    }

    private fun googleSignIn(
        rawNonce: String,
        googleIdToken: String
    ) {
        viewModelScope.launch {
            when(val result = googleSignInUseCase(rawNonce, googleIdToken)) {
                is Resource.Success -> {
                    _loginState.value = LoginState.Success
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(result.uiText)
                }
            }
        }
    }
}