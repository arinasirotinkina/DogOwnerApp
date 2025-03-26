package com.example.dogownerapp.presentation.screen.care

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import com.yandex.mapkit.geometry.Point


@Composable
fun Veterinary(navController: NavController)  {
    val context = LocalContext.current

    // Используем AndroidView для отображения MapView
    AndroidView(factory = { context ->
        MapKitFactory.setApiKey("66102353-02dd-4479-b3b6-5f242c2192c1")
        MapKitFactory.initialize(context)
        // Здесь мы точно уверены, что MapKit уже инициализирован
        val mapView = MapView(context)

        // Настройка начальной камеры
        val initialPosition = CameraPosition(Point(55.751244, 37.618423), 10f, 0f, 0f)
        mapView.map.move(initialPosition)

        // Добавление маркеров для клиник
        val clinics = listOf(
            Point(55.751244, 37.618423), // Пример клиники
            Point(55.753244, 37.617423)  // Другая клиника
        )

        // Добавление маркеров на карту
        val mapObjects = mapView.map.mapObjects
        clinics.forEach { clinic ->
            mapObjects.addPlacemark(clinic)
        }

        mapView // Возвращаем MapView для отображения
    })
}