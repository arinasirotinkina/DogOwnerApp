package com.example.dogownerapp.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogownerapp.domain.usecase.auth.LogOutUseCase
import com.example.dogownerapp.domain.usecase.auth.LoginUseCase
import com.example.dogownerapp.domain.usecase.auth.RegistrationUseCase
import com.example.dogownerapp.presentation.auth.AuthViewModel

class AuthViewModelFactory(
    private val registerUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase,
    private val logOutUseCase: LogOutUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(registerUseCase, loginUseCase, logOutUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
