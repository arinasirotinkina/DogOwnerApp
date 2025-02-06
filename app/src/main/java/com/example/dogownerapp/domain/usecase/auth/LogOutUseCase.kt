package com.example.dogownerapp.domain.usecase.auth

import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor (private var repository: AuthRepository) {
    suspend fun execute() {
        repository.logout()
    }
}