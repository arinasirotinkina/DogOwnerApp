package com.example.dogownerapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.data.repository.AuthRepositoryImpl
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.usecase.auth.RegisterUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUser,
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> get() = _authResult


    fun register(email: String, password: String) {
        viewModelScope.launch {
            registerUseCase.execute(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }

}
