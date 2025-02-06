package com.example.dogownerapp.domain.usecase.auth

import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor (private val repository: AuthRepository) {
    suspend fun execute(email: String, password: String): Flow<AuthResult> {
        return repository.login(email, password)
    }
}