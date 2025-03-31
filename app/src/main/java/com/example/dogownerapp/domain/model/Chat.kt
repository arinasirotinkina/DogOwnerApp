package com.example.dogownerapp.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val name: String = "")
{

}
data class Message(
    val id: String = "",
    val text: String = "",
    val sender: Boolean = true,
    val timestamp: Timestamp = Timestamp.now()
)