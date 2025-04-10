package com.example.dogownerapp.di

import com.example.dogownerapp.data.repository.ChatRepositoryImpl
import com.example.dogownerapp.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ChatRepositoryModule {

    @Provides
    fun provideChatRepository(dataSource: FirebaseFirestore, auth: FirebaseAuth): ChatRepository {
        return ChatRepositoryImpl(dataSource, auth)
    }
}