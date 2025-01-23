package com.example.dogownerapp.presentation.ui.health

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.repository.DogRepository

class HealthViewModel : ViewModel() {
    private val dogRepository = DogRepository()

    private val _dogs = MutableLiveData<List<Dog>>()
    val dogs: LiveData<List<Dog>> get() = _dogs

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