package com.example.dogownerapp.presentation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dogownerapp.R
import com.example.dogownerapp.databinding.ActivityMapsBinding
import com.example.dogownerapp.databinding.ActivityShowLocaleMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.runBlocking
import java.util.Locale
class ShowLocaleMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowLocaleMapsBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapObjects: MapObjectCollection  // Инициализируем переменную
    private var geoPoint: GeoPoint? = null  // Объект GeoPoint для переданных координат

    private val requestPermissionLocationLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                binding.mapview.map.move(
                    CameraPosition(
                        Point(55.753215, 37.622504),
                        10.0f,
                        0.0f,
                        0.0f
                    )
                )
            } else {
                showAtPoint()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)

        binding = ActivityShowLocaleMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализируем переменную mapObjects здесь
        mapObjects = binding.mapview.map.mapObjects

        // Получаем переданные координаты из Intent
        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)

        // Если координаты не 0.0, отображаем точку на карте
        if (latitude != 0.0 && longitude != 0.0) {
            setMarkerLocation(Point(latitude, longitude))
        } else {
            // Если координаты не переданы, показываем текущую локацию пользователя
            showAtPoint()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.mapview.map.isRotateGesturesEnabled = true
    }

    private fun setMarkerLocation(location: Point) {
        mapObjects.clear()
        val placemarkMapObject = mapObjects.addPlacemark(
            location,
            ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.placemark_icon))
        )
        placemarkMapObject.opacity = 0.7f
        binding.mapview.map.move(
            CameraPosition(
                location,
                15.0f, // Зум на уровне 15 (можно изменять в зависимости от нужд)
                0.0f,
                0.0f
            )
        )
    }

    private fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private var isLocationUpdated = false

    private fun showAtPoint() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            val locationManager: LocationManager =
                this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val locationRequest =
                    com.google.android.gms.location.LocationRequest.create().apply {
                        priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                        interval = 5000
                    }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        if (!isLocationUpdated) { // Проверяем, обновляли ли мы местоположение ранее
                            locationResult.lastLocation?.let { location ->
                                val userLatLng = LatLng(location.latitude, location.longitude)
                                binding.mapview.map.move(
                                    CameraPosition(
                                        Point(userLatLng.latitude, userLatLng.longitude),
                                        15.0f,
                                        0.0f,
                                        0.0f
                                    )
                                )

                                isLocationUpdated = true // Ставим флаг, чтобы больше не обновлять
                                fusedLocationClient.removeLocationUpdates(this) // Останавливаем обновления
                            }
                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

            } else {
                binding.mapview.map.move(
                    CameraPosition(Point(55.753215, 37.622504), 10.0f, 0.0f, 0.0f)
                )
            }
        } else {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
