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
    private var adressPoint: Point? = null
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

/*private fun showAtPoint() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
            interval = 5000
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    binding.mapview.map.move(
                        CameraPosition(Point(userLatLng.latitude, userLatLng.longitude), 15.0f, 0.0f, 0.0f)
                    )
                    Toast.makeText(this@MapsActivity, "Ваши координаты: ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)

        /*
    val locationManager: LocationManager =
        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                binding.mapview.map.move(
                    CameraPosition(
                        Point(
                            userLatLng.latitude,
                            userLatLng.longitude
                        ), 15.0f, 0.0f, 0.0f
                    )
                )
                Toast.makeText(this, location.longitude.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        binding.mapview.map.move(
            CameraPosition(
                Point(55.753215, 37.622504), 10.0f, 0.0f, 0.0f
            )
        )
    }*/
    } else {
        requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
}

/*import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dogownerapp.R
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MapsActivity : AppCompatActivity() {
private val REQUEST_LOCATION_PERMISSION = 1
private lateinit var mapView: MapView
private val point = Point(55.754186, 37.650085)

private val placemarkTapListener = MapObjectTapListener { _, point ->
    Toast.makeText(
        this,
        "Точка на карте: (${point.latitude}, ${point.longitude})",
        Toast.LENGTH_SHORT
    ).show()
    true
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }
    MapKitFactory.setApiKey("66102353-02dd-4479-b3b6-5f242c2192c1")

    MapKitFactory.initialize(this)
    setContentView(R.layout.activity_maps)
    mapView = findViewById(R.id.mapview)
    mapView.mapWindow.map.let { map ->
        map.move(
            CameraPosition(
                point,
                17.0f,
                150.0f,
                30.0f
            )
        )
    }
    mapView.map.move(
        CameraPosition(Point(55.751244, 37.618423), 12.0f, 0.0f, 0.0f)
    )

    searchVeterinaryClinics()

    /* mapView.mapWindow.map.let { map ->
        map.move(
            CameraPosition(
                point,
                17.0f,
                150.0f,
                30.0f
            )
        )
        val imageProvider = ImageProvider.fromResource(this, R.drawable.placemark_icon)
        val placemark = map.mapObjects.addPlacemark(point).apply {
            setIcon(imageProvider)
        }
        placemark.addTapListener(placemarkTapListener)
    }*/
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_LOCATION_PERMISSION) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Разрешение получено
        } else {
            // Разрешение отклонено, можно показать сообщение пользователю
        }
    }
}
override fun onStart() {
    super.onStart()
    MapKitFactory.getInstance().onStart()
    mapView.onStart()
}

override fun onStop() {
    mapView.onStop()
    MapKitFactory.getInstance().onStop()
    super.onStop()
}
private fun searchVeterinaryClinics() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://search-maps.yandex.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(YandexPlacesService::class.java)

    val call = api.searchPlaces(
        text = "вет",
        apikey = "66102353-02dd-4479-b3b6-5f242c2192c1",
        lang = "ru_RU"
    )

    call.enqueue(object : Callback<PlacesResponse> {
        override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
            val places = response.body()?.features
            Log.i("YandexPlaces", places?.size.toString())
            places?.forEach { place ->
                val coords = place.geometry.coordinates
                Log.d("YandexPlaces", "Добавляем метку: ${coords[1]}, ${coords[0]}")
                val point = Point(coords[1], coords[0])
                mapView.map.mapObjects.addPlacemark(point)
            }

        }

        override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
            Log.e("YandexPlaces", "Ошибка: ${t.message}")
        }
    })
}
}
interface YandexPlacesService {
@GET("v1/")
fun searchPlaces(
    @Query("text") text: String,
    @Query("apikey") apikey: String,
    @Query("lang") lang: String,
    @Query("ll") ll: String = "37.6173,55.7558", // Москва
    @Query("spn") spn: String = "0.5,0.5",
    @Query("type") type: String = "biz",
    @Query("results") results: Int = 10
): Call<PlacesResponse>
}
data class PlacesResponse(val features: List<Feature>)
data class Feature(val geometry: Geometry)
data class Geometry(val coordinates: List<Double>)

*/
/*

class MapsActivity : AppCompatActivity() {
private lateinit var mapView: MapView
private val point = Point(55.754186, 37.650085)
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    //checkAndRequestLocationPermission()
    MapKitFactory.setApiKey("66102353-02dd-4479-b3b6-5f242c2192c1")
    MapKitFactory.initialize(this)
    setContentView(R.layout.activity_maps)
    mapView = findViewById<MapView>(R.id.mapView)

    mapView.mapWindow.map.let { map ->
        map.move(
            CameraPosition(
                point,
                17.0f,
                150.0f,
                30.0f
            )
        )
    }
    //mapView.map.move(
        //CameraPosition(Point(55.751244, 37.618423), 12.0f, 0.0f, 0.0f)
    //)

    //searchVeterinaryClinics()

}
override fun onStart() {
    super.onStart()
    MapKitFactory.getInstance().onStart()
    mapView.onStart()
}

override fun onStop() {
    mapView.onStop()
    MapKitFactory.getInstance().onStop()
    super.onStop()
}

private fun searchVeterinaryClinics() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://search-maps.yandex.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(YandexPlacesService::class.java)

    val call = api.searchPlaces(
        text = "ветеринарная клиника",
        apikey = "66102353-02dd-4479-b3b6-5f242c2192c1",
        lang = "ru_RU"
    )

    call.enqueue(object : Callback<PlacesResponse> {
        override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
            val places = response.body()?.features
            places?.forEach { place ->
                val coords = place.geometry.coordinates
                val point = Point(coords[1], coords[0])
                mapView.map.mapObjects.addPlacemark(point)
            }
        }

        override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
            Log.e("YandexPlaces", "Ошибка: ${t.message}")
        }
    })
}
}
interface YandexPlacesService {
@GET("v1/")
fun searchPlaces(
    @Query("text") text: String,
    @Query("apikey") apikey: String,
    @Query("lang") lang: String,
    @Query("ll") ll: String = "37.6173,55.7558", // Москва
    @Query("spn") spn: String = "0.5,0.5",
    @Query("type") type: String = "biz",
    @Query("results") results: Int = 10
): Call<PlacesResponse>
}
data class PlacesResponse(val features: List<Feature>)
data class Feature(val geometry: Geometry)
data class Geometry(val coordinates: List<Double>)
*/