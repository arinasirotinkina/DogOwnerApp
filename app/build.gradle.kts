plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.dogownerapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dogownerapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // Убедитесь, что эта версия актуальна

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.amazonaws:aws-android-sdk-s3:2.22.0")
    implementation("com.amazonaws:aws-android-sdk-core:2.22.0")
    implementation("commons-net:commons-net:3.8.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.runtime.android)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.maps)
    // implementation(libs.androidx.benchmark.baseline.profile.gradle.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.7.4"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.14")
    debugImplementation(libs.androidx.ui.tooling)
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("com.yandex.android:maps.mobile:4.10.1-lite")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //implementation ("com.yandex.android:maps.mobile:4.0.0")
    //implementation("com.google.dagger:hilt-android:2.48")
    //kapt("com.google.dagger:hilt-compiler:2.48")
    //implementation("com.google.firebase:firebase-core:16.0.1")
    //implementation("com.google.firebase:firebase-messaging:17.4.0")
    // Import the Firebase BoM
    //implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    //implementation("com.google.firebase:firebase-bom:32.7.0")
    //implementation("com.google.firebase:firebase-messaging")

    // Основные зависимости для Jetpack Compose
    //implementation("androidx.compose.main:main:1.5.14")  // Основной UI Compose
    //implementation("androidx.compose.material:material:1.5.14")  // Material Components
    //implementation("androidx.compose.main:main-tooling-preview:1.5.14")  // Для предварительного просмотра UI
    //debugImplementation("androidx.compose.main:main-tooling:1.5.14") // Для дебага

    // Навигация Compose (если используется)
    //implementation("androidx.navigation:navigation-compose:2.5.0")  // Для навигации в Compose

    // Зависимости для ViewModel и LiveData (если используется)
    //implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    //implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
}
