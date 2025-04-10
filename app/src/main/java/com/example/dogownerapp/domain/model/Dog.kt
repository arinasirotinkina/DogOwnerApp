package com.example.dogownerapp.domain.model

data class Dog(
    val id: String = "",
    val name: String = "",
    val breed: String = "",
    val birthDate: String = "",
    val gender: Gender = Gender.MALE,
    var weight: Double = 0.0,
    val castration: Boolean = false,
    val sterilization: Boolean = false,
    val vaccinations: List<Vaccination> = mutableListOf(),
    val treatments: List<Treatment> = mutableListOf()
) {
    constructor() : this("", "", "", "",
        Gender.MALE, 0.0, false, false)

    fun getVaccinationsList() : String {
        var listString = ""
        for (vaccination in vaccinations) {
            listString += vaccination.name + " - " + vaccination.date + "\n"
        }
        return listString
    }
    fun getTreatmentsList() : String {
        var listString = ""
        for (treatment in treatments) {
            listString += treatment.name + " - " + treatment.date + "\n"
        }
        return listString
    }
}

enum class Gender {
    MALE,
    FEMALE
}

data class Vaccination(
    val name: String = "",
    val date: String = ""
)

data class Treatment(
    val name: String = "",
    val date: String = ""
)
