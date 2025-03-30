package com.example.dogownerapp.di

import com.example.dogownerapp.data.repository.AuthRepositoryImpl
import com.example.dogownerapp.data.repository.SpecialistRepositoryImpl
import com.example.dogownerapp.data.repository.UserRepositoryImpl
import com.example.dogownerapp.domain.repository.SpecialistRepository
import com.example.dogownerapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SpecialistRepositoryModule {

    @Provides
    fun provideSpecialistRepository(dataSource: FirebaseFirestore, auth: FirebaseAuth): SpecialistRepository {
        return SpecialistRepositoryImpl(dataSource, auth)
    }
}