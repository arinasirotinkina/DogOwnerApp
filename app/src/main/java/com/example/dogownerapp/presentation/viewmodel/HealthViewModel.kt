package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.model.Dog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val dogListInteractor: DogListInteractor
) : ViewModel() {

    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs.asStateFlow()

    init {
        loadDogs()
    }

    private fun loadDogs() {
        viewModelScope.launch {
            dogListInteractor.loadDogs().collect { dogList ->
                _dogs.value = dogList
            }
        }
    }

    fun addDog(dog: Dog, dogId: String) {
        viewModelScope.launch {
            dogListInteractor.addDog(dog, dogId)
        }
    }

    fun removeDog(dogId: String) {
        viewModelScope.launch {
            dogListInteractor.removeDog(dogId)
        }
    }

    fun updateDog(dog: Dog, dogId: String) {
        viewModelScope.launch {
            dogListInteractor.updateDog(dog, dogId)
        }
    }
}
