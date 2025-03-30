package com.example.dogownerapp

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class DogOwnerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("66102353-02dd-4479-b3b6-5f242c2192c1")

    }
}