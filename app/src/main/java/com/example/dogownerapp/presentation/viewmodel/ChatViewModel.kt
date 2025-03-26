package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class ChatViewModel() : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()


     fun loadMessages(chatId: String) {
        db.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val messageList = snapshot?.documents?.map { doc ->
                    Message(doc.id, doc.getString("text") ?: "", doc.getString("sender") ?: "")
                } ?: emptyList()
                _messages.value = messageList
            }
    }

    fun sendMessage(text: String, sender: String, chatId: String) {
        val newMessage = hashMapOf("text" to text, "sender" to sender, "timestamp" to System.currentTimeMillis())
        db.collection("chats").document(chatId).collection("messages").add(newMessage)
    }
}

