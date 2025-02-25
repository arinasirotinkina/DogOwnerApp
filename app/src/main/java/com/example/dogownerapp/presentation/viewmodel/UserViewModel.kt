package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.interactor.UserInteractor
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : ViewModel() {
    private val _user = MutableStateFlow<User>(User())
    var user: StateFlow<User> = _user.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            userInteractor.loadUser().collect { user ->
                _user.value = user
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userInteractor.updateUser(user)
        }
    }

}