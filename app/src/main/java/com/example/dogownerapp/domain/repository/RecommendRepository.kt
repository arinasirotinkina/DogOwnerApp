package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Recommendation
import kotlinx.coroutines.flow.Flow

interface RecommendRepository {
    fun getRecommends(): Flow<List<Recommendation>>
    fun getRecommendById(recId: String): Flow<Recommendation>
}