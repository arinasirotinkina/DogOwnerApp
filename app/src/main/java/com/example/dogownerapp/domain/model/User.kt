package com.example.dogownerapp.domain.model

import com.google.firebase.firestore.GeoPoint

data class User(
    val id : String = "",
    val name: String = "Имя",
    val surname: String = "Фамилия",
    val email: String = "email",
    val phoneNumber: String = "+7...",
    val adress: String = "-",
    val role: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val favourites: List<String> = emptyList(),

) {

}