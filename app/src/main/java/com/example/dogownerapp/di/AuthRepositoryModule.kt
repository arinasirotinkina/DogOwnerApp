package com.example.dogownerapp.di

import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.data.repository.AuthRepositoryImpl
import com.example.dogownerapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    fun provideAuthRepository(dataSource: FirebaseAuthDataSource): AuthRepository {
        return AuthRepositoryImpl(dataSource)
    }
}