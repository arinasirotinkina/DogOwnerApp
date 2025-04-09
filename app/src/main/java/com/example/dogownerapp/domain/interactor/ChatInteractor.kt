package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Chat
import com.example.dogownerapp.domain.model.Message
import com.example.dogownerapp.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatInteractor @Inject constructor (private val repository: ChatRepository) {
    fun getChats(owner:Boolean) : Flow<List<Chat>> {
        return if (owner) {
            repository.getChatsOwner()
        } else {
            repository.getChatsSpec()
        }
    }

    fun getMessages(owner: Boolean, personId:String) : Flow<List<Message>> {
        return if (owner) {
            repository.getFromOwner(personId)
        } else {
            repository.getFromSpec(personId)
        }
    }
    suspend fun sendMessage(owner: Boolean, personId: String, message: Message) {
        if (owner) {
            repository.sendFromOwner(personId, message)
        } else {
            repository.sendFromSpec(personId, message)
        }
    }
}
