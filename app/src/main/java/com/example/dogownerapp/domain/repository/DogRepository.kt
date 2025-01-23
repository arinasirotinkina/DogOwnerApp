package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Dog

class DogRepository {
    private val dogList = mutableListOf<Dog>()

    fun getDogs(): List<Dog> = dogList

    fun addDog(dog: Dog) {
        dogList.add(dog)
    }

    fun removeDog(dog: Dog) {
        dogList.remove(dog)
    }
}