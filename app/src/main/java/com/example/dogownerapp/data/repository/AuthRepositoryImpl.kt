package com.example.dogownerapp.data.repository

import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(private val dataSource: FirebaseAuthDataSource) : AuthRepository {
    override suspend fun login(email: String, password: String): Flow<AuthResult> {
        return dataSource.login(email, password)
    }

    override suspend fun register(email: String, password: String): Flow<AuthResult> {
        return dataSource.register(email, password)
    }

    override suspend fun logout() {
        return dataSource.logout()
    }

}