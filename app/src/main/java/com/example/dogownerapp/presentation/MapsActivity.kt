package com.example.dogownerapp.presentation

import android.Manifest
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