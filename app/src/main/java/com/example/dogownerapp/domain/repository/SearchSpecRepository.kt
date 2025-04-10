package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Specialist
import kotlinx.coroutines.flow.Flow

interface SearchSpecRepository {
    fun getSpecs(): Flow<List<Specialist>>
    fun getSpecById(specId: String): Flow<Specialist>
}