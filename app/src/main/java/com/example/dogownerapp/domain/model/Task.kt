package com.example.dogownerapp.domain.model

import android.telephony.mbms.StreamingServiceInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

data class Task(
    val id: String = "",
    val date: String = "",  // Храним дату как строку
    val name: String = "",
    val description: String = ""
) {
    fun getLocalDate(): LocalDate {
        return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
