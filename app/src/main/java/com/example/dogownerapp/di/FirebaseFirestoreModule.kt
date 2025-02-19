package com.example.dogownerapp.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class FirebaseFirestoreModule {
}
@Module
@InstallIn(SingletonComponent::class) // Доступен во всём приложении
object FirebaFirebaseFirestoreModuleseModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

}