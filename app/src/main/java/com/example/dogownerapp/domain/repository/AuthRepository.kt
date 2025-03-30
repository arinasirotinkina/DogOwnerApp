package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<AuthResult>
    suspend fun register(email: String, password: String): Flow<AuthResult>
    suspend fun registerSpecialist(name: String, surname:String, email: String,
                                   password: String): Flow<AuthResult>
    suspend fun logout()
    fun isAuthorized() : Boolean
    fun isOwner() : Flow<Boolean>
    fun getUser(): Flow<User>
    suspend fun updateUser(user: User)
}