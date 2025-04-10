package com.example.dogownerapp.presentation

import android.widget.Toast
import com.example.dogownerapp.R
import com.example.dogownerapp.databinding.ActivityMapsBinding
import com.google.common.net.MediaType.JWT
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
import com.google.android.gms.maps.model.LatLng
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint

import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.runBlocking
import java.util.*


class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private var address: String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapObjects: MapObjectCollection

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
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapview.map.addTapListener(tapListener)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.mapview.map.isRotateGesturesEnabled = true
        mapObjects = binding.mapview.map.mapObjects

        showAtPoint()

        binding.saveAdress.setOnClickListener {
            if (address != null) {
                runBlocking {
                    saveEditedData(address)
                }
                val resultIntent = Intent().apply {
                    putExtra("selected_address", address)
                    putExtra("selected_latitude", latitude)
                    putExtra("selected_longitude", longitude)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Выберите адрес чтобы сохранить!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val tapListener = GeoObjectTapListener { geoObjectTapEvent ->
        setMarkerLocation(geoObjectTapEvent.geoObject.geometry[0].point!!)
        getAddressForLocation(geoObjectTapEvent.geoObject.geometry[0].point!!)
        false
    }

    private fun setMarkerLocation(location: Point) {
        mapObjects.clear()
        val placemarkMapObject = mapObjects.addPlacemark(
            Point(location.latitude, location.longitude),
            ImageProvider.fromBitmap(createBitmapFromVector(R.drawable.placemark_icon))
        )
        placemarkMapObject.opacity = 0.7f
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

    private fun saveEditedData(address: String?) {
        Toast.makeText(this, address, Toast.LENGTH_SHORT).show()
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

    private fun getAddressForLocation(point: Point) {
        val geocoder = Geocoder(this, Locale("ru", "RU"))
        val addressGeo = geocoder.getFromLocation(point.latitude, point.longitude, 1)
        address = addressGeo?.get(0)?.getAddressLine(0)
        latitude = point.latitude
        longitude = point.longitude
        Log.d("Address", address.toString())
        binding.textView2.text = address.toString()
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
                                Toast.makeText(
                                    this@MapsActivity,
                                    "Ваши координаты: ${location.latitude}, ${location.longitude}",
                                    Toast.LENGTH_SHORT
                                ).show()

                                isLocationUpdated = true
                                fusedLocationClient.removeLocationUpdates(this)
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