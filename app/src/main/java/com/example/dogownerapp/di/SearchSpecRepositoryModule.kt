package com.example.dogownerapp.di

import com.example.dogownerapp.data.repository.RecommendRepositoryImpl
import com.example.dogownerapp.data.repository.SearchSpecRepositoryImpl
import com.example.dogownerapp.domain.repository.RecommendRepository
import com.example.dogownerapp.domain.repository.SearchSpecRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SearchSpecRepositoryModule {

    @Provides
    fun provideSearchSpecRepository(dataSource: FirebaseFirestore, auth: FirebaseAuth): SearchSpecRepository {
        return SearchSpecRepositoryImpl(dataSource, auth)
    }
}
