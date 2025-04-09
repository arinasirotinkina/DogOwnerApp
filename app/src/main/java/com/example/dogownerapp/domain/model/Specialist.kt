package com.example.dogownerapp.domain.model

import com.google.firebase.firestore.GeoPoint

data class Specialist (
    var id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val specialization: String = "",
    val address: String = "",
    val about: String = "",
    val experience: String = "",
    val conditions: String = "",
    val prices: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),

    ) {

}