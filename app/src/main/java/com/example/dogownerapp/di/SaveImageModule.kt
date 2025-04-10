package com.example.dogownerapp.di

import com.example.dogownerapp.data.datasource.SaveImageServiceImpl
import com.example.dogownerapp.domain.repository.SaveImageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaveImageModule {

    @Provides
    @Singleton
    fun provideSaveImage(): SaveImageService =
        SaveImageServiceImpl("arinas8t_h", "H123h456!")
}
