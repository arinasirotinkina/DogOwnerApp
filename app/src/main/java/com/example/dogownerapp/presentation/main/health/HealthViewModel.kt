package com.example.dogownerapp.presentation.main.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.repository.DogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(): ViewModel() {
    private val dogRepository = DogRepository()
    private val _dogs = MutableStateFlow<List<Dog>>(emptyList())
    val dogs: StateFlow<List<Dog>> = _dogs

    init {
        loadDogs()
    }

    private fun loadDogs() {
        _dogs.value = dogRepository.getDogs()
    }

    fun addDog(dog: Dog) {
        dogRepository.addDog(dog)
        _dogs.value = dogRepository.getDogs()
    }

    fun removeDog(dog: Dog) {
        dogRepository.removeDog(dog)
        _dogs.value = dogRepository.getDogs()
    }
}