package com.example.dogownerapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class DogOwnerApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}