package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Chat(val id: String = "", val name: String = "")
data class Message(val id: String = "", val text: String = "", val sender: String = "")


class ChatListViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        db.collection("chats").addSnapshotListener { snapshot, _ ->
            val chatList = snapshot?.documents?.map { doc ->
                Chat(doc.id, doc.getString("name") ?: "")
            } ?: emptyList()
            _chats.value = chatList
        }
    }
}