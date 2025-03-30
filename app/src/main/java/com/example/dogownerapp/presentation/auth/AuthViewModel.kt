package com.example.dogownerapp.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogownerapp.domain.interactor.AuthInteractor
import com.example.dogownerapp.domain.interactor.UserInteractor
import com.example.dogownerapp.domain.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : ViewModel() {

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> get() = _authResult

    private val _owner = MutableStateFlow<Boolean>(true)
    val owner: StateFlow<Boolean> get() = _owner

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authInteractor.register(email, password).collectLatest {
                _authResult.value = it
            }
        }
    }
    fun registerSpecialist(name: String, surname: String, email: String, password: String) {
        viewModelScope.launch {
            authInteractor.registerSpecialist(name, surname, email, password)
                .collectLatest {
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
    fun isAuthorized() : Boolean {
        return authInteractor.isAuthorized()
    }


    suspend fun checkOwner() {
         Log.i("roleVM", "f".toString())
         authInteractor.isOwner().take(3).collect {
             _owner.value = it

         }
    }

}
