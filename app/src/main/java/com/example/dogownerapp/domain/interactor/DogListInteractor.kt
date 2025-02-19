package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.repository.AuthRepository
import com.example.dogownerapp.domain.repository.DogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DogListInteractor @Inject constructor (private val repository: DogRepository) {
    fun loadDogs(): Flow<List<Dog>> {
        return repository.getDogs()
    }


    fun getDogById(dogId: String): Flow<Dog> {
        return repository.getDogById(dogId)
    }

    suspend fun addDog(dog: Dog) {
        repository.addDog(dog)
    }

    suspend fun removeDog(dogId: String) {
        repository.removeDog(dogId)
    }

    suspend fun updateDog(dog: Dog, dogId: String) {
        repository.updateDog(dog, dogId)
    }
}