package com.example.dogownerapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.AuthInteractor
import com.example.dogownerapp.domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> get() = _authResult


    fun register(email: String, password: String) {
        viewModelScope.launch {
            authInteractor.register(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authInteractor.login(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authInteractor.logout()
        }
    }

}
