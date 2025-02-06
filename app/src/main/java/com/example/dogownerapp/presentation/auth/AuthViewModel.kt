package com.example.dogownerapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.usecase.auth.LogOutUseCase
import com.example.dogownerapp.domain.usecase.auth.LoginUseCase
import com.example.dogownerapp.domain.usecase.auth.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> get() = _authResult


    fun register(email: String, password: String) {
        viewModelScope.launch {
            registrationUseCase.execute(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase.execute(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logOutUseCase.execute()
        }
    }

}
