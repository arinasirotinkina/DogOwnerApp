package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.SpecialistRepository
import com.example.dogownerapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpecialistInteractor @Inject constructor (private val repository: SpecialistRepository){
    fun loadSpecialist(): Flow<Specialist> {
        return repository.getSpecialist()
    }
    suspend fun updateSpecialist(spec: Specialist) {
        repository.updateSpecialis(spec)
    }
}