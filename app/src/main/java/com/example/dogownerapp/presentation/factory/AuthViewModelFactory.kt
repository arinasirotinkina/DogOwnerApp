package com.example.dogownerapp.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dogownerapp.domain.usecase.auth.RegisterUser
import com.example.dogownerapp.presentation.auth.RegisterViewModel

class AuthViewModelFactory(
    private val registerUseCase: RegisterUser,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registerUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
