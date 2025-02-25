package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.DogRepository
import com.example.dogownerapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor (private val repository: UserRepository) {
    fun loadUser(): Flow<User> {
        return repository.getUser()
    }
    suspend fun updateUser(user: User) {
        repository.updateUser(user)
    }

}