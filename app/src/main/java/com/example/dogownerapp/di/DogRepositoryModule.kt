package com.example.dogownerapp.di

import com.example.dogownerapp.data.repository.DogRepositoryImpl
import com.example.dogownerapp.domain.repository.DogRepository
import com.example.dogownerapp.domain.repository.SaveImageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DogRepositoryModule {

    @Provides
    fun provideDogRepository(dataSource: FirebaseFirestore,
                             auth: FirebaseAuth, imageService: SaveImageService)
    : DogRepository {
        return DogRepositoryImpl(dataSource, auth, imageService)
    }
}