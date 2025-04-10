package com.example.dogownerapp.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Task(
    val id: String = "",
    val date: String = "",
    val name: String = "",
    val description: String = ""
) {
    fun getLocalDate(): LocalDate {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
