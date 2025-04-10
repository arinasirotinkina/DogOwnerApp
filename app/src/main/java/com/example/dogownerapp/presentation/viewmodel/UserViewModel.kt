package com.example.dogownerapp.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.interactor.UserInteractor
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.SaveImageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val saveImageService: SaveImageService
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

    fun uploadPhoto(uri: Uri, context: Context, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                saveImageService.uploadFileToFTP(uri, context, "users", _user.value.id)
                onComplete()
            } catch (e: Exception) {
                Log.e("UploadPhoto", "Ошибка загрузки фото", e)
            }
        }
    }
    fun updateFavs(favourites: List<String>) {
        viewModelScope.launch {
            userInteractor.updateFavs(favourites)
        }
    }
}