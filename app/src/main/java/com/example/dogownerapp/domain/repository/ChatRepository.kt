package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Chat
import com.example.dogownerapp.domain.model.Message
import com.example.dogownerapp.domain.model.Specialist
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    fun getChatsOwner(): Flow<List<Chat>>
    fun getChatsSpec(): Flow<List<Chat>>
    fun getFromOwner(specId: String): Flow<List<Message>>
    fun getFromSpec(ownerId: String): Flow<List<Message>>
    suspend fun sendFromOwner(specId: String, message: Message)
    suspend fun sendFromSpec(ownerId: String, message: Message)
}