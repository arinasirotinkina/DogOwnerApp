package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.repository.RecommendRepository
import com.example.dogownerapp.domain.repository.SearchSpecRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSpecInteractor @Inject constructor (private val repository: SearchSpecRepository) {
    fun loadSpecs(): Flow<List<Specialist>> {
        return repository.getSpecs()
    }
}