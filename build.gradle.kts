
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    //id("com.google.gms.google-services") version "4.4.2" apply false
}
buildscript {
    repositories {
        google()  // Это необходимо для Jetpack Compose и других зависимостей от Google
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}

