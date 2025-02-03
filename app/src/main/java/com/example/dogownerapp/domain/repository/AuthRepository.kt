package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<AuthResult>
    suspend fun register(email: String, password: String): Flow<AuthResult>
    suspend fun logout()
}