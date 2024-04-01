package com.example.olpgas.auth.presentation.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.olpgas.auth.domain.use_case.LoginUseCases
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.core.util.NetworkConnectivityObserver
import com.example.olpgas.core.util.Resource
import com.example.olpgas.core.util.domain.states.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCases: LoginUseCases,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _emailState:MutableLiveData<TextFieldState> = MutableLiveData(TextFieldState())
    val emailState: LiveData<TextFieldState> = _emailState

    private val _passwordState = MutableLiveData(TextFieldState())
    val passwordState: LiveData<TextFieldState> = _passwordState

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    val connectionStatus = connectivityObserver.observe().asLiveData()

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
            _loginState.value = LoginState.Loading(isLoading = true)
            _emailState.value = emailState.value?.copy(
                error = null
            )
            _passwordState.value = passwordState.value?.copy(
                error = null
            )

            val loginResult = loginUseCases.loginUseCase(
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
                    runBlocking {
                        loginUseCases.setUserProfileLocalCacheUseCase()
                    }
                    _loginState.value = LoginState.Success
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(loginResult.result.uiText)
                }
                null -> {}
            }
            _loginState.value = LoginState.Loading(isLoading = false)
        }
    }

    private fun googleSignIn(
        rawNonce: String,
        googleIdToken: String
    ) {
        viewModelScope.launch {
            when(val result = loginUseCases.googleSignInUseCase(rawNonce, googleIdToken)) {
                is Resource.Success -> {
                    runBlocking {
                        loginUseCases.setUpUserWithGoogleUseCase()
                        loginUseCases.setUserProfileLocalCacheUseCase()
                    }
                    _loginState.value = LoginState.Success
                }
                is Resource.Error -> {
                    _loginState.value = LoginState.Error(result.uiText)
                }
            }
        }
    }
}