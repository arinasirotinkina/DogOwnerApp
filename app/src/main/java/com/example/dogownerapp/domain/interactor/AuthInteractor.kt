package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor (private val repository: AuthRepository) {
    suspend fun register(name: String, surname: String,
                         email: String, password: String): Flow<AuthResult> {
        return repository.register(name, surname, email, password)
    }
    suspend fun login(email: String, password: String): Flow<AuthResult> {
        return repository.login(email, password)
    }
    suspend fun logout() {
        repository.logout()
    }
    suspend fun registerSpecialist(name: String, surname: String,
                                   email: String, password: String) : Flow<AuthResult>{
        return repository.registerSpecialist(name, surname, email, password)
    }
    fun isAuthorized() : Boolean {
        return repository.isAuthorized()
    }

}