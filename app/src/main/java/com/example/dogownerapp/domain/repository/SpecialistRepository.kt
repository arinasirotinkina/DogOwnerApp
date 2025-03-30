package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SpecialistRepository {
    fun getSpecialist(): Flow<Specialist>
    suspend fun updateSpecialis(specialist: Specialist)
}