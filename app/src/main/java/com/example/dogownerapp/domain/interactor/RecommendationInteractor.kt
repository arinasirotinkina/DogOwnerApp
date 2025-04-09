package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.domain.repository.RecommendRepository
import com.example.dogownerapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class RecommendationInteractor @Inject constructor (private val repository: RecommendRepository) {
    fun loadRecommends(): Flow<List<Recommendation>> {
        return repository.getRecommends()
    }
}