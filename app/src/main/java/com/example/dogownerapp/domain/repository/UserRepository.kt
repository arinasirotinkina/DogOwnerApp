package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User>
    suspend fun updateUser(user: User)
}