package com.example.dogownerapp.models

import java.util.Date

data class Dog(
    val name: String,
    val breed: String,
    val birthDate: Date,
    val gender: Gender,
    var weight: Double,
    val castration: Boolean,
    val sterilization: Boolean,
    val vaccinations: MutableList<Vaccination> = mutableListOf(),
    val treatments: MutableList<Treatment> = mutableListOf()
) {
    fun addVaccination(vaccination: Vaccination) {
        vaccinations.add(vaccination)
    }

    fun removeVaccination(vaccination: Vaccination) {
        vaccinations.remove(vaccination)
    }

    fun addTreatment(treatment: Treatment) {
        treatments.add(treatment)
    }

    fun removeTreatment(treatment: Treatment) {
        treatments.remove(treatment)
    }

    fun getVaccinations(): List<Vaccination> = vaccinations
    fun getTreatments(): List<Treatment> = treatments
}

enum class Gender {
    MALE,
    FEMALE
}

data class Vaccination(
    val name: String,
    val date: Date
)

data class Treatment(
    val name: String,
    val date: Date
)

