package com.example.dogownerapp.domain.usecase.auth

import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class RegisterUser(private val repository: AuthRepository) {
    suspend fun execute(email: String, password: String): Flow<AuthResult> {
        return repository.register(email, password)
    }
}