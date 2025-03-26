package com.example.dogownerapp.domain.model

data class Recommendation(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val author: String = "",
    val health: Boolean = false,
    val breeds: Boolean = false,
    val care: Boolean = false,
    val nutrition: Boolean = false,
    val education: Boolean = false
) {

}