package com.example.dogownerapp.di

import com.example.dogownerapp.data.repository.DogRepositoryImpl
import com.example.dogownerapp.data.repository.TaskRepositoryImpl
import com.example.dogownerapp.data.repository.UserRepositoryImpl
import com.example.dogownerapp.domain.repository.DogRepository
import com.example.dogownerapp.domain.repository.TaskRepository
import com.example.dogownerapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Provides
    fun provideUserRepository(dataSource: FirebaseFirestore, auth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(dataSource, auth)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TaskRepositoryModule {

    @Provides
    fun provideTaskRepository(dataSource: FirebaseFirestore, auth: FirebaseAuth): TaskRepository {
        return TaskRepositoryImpl(dataSource, auth)
    }
}
