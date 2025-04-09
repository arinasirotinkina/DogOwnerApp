package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Dog
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    fun getDogs(): Flow<List<Dog>>
    fun getDogById(dogId: String): Flow<Dog>
    suspend fun addDog(dog: Dog, dogId: String)
    suspend fun removeDog(dogId: String)
    suspend fun updateDog(dog: Dog, dogId: String)
}
