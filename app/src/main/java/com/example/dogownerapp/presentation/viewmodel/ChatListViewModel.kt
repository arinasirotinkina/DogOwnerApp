package com.example.dogownerapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dogownerapp.domain.interactor.ChatInteractor
import com.example.dogownerapp.domain.model.Chat
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(private val interactor: ChatInteractor)
    : ViewModel() {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats = _chats.asStateFlow()

    fun loadChats(owner: Boolean) {
        viewModelScope.launch {
            interactor.getChats(owner).collect { chatsList ->
                _chats.value = chatsList

            }
        }
    }
}