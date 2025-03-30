package com.example.dogownerapp.domain.model

data class User(
    val name: String = "Имя",
    val email: String = "email",
    val phoneNumber: String = "+7...",
    val adress: String = "-",
    val birthDate: String = "00.00.0000",
    val role: String = ""
) {

}