package com.example.dogownerapp.models

import java.util.Date

data class Dog(
    val name: String,
    val breed: String,
    val birthDate: Date,
    val gender: Gender,
    var weight: Double,
    val vaccinations: MutableList<Vaccination> = mutableListOf()
) {
    // Метод для добавления прививки
    fun addVaccination(vaccination: Vaccination) {
        vaccinations.add(vaccination)
    }

    // Метод для удаления прививки
    fun removeVaccination(vaccination: Vaccination) {
        vaccinations.remove(vaccination)
    }

    // Метод для получения списка всех прививок
    fun getVaccinations(): List<Vaccination> = vaccinations
}

// Перечисление для пола собаки
enum class Gender {
    MALE,
    FEMALE
}

// Класс для прививки
data class Vaccination(
    val name: String,
    val date: Date
)
