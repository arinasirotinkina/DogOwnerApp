package com.example.dogownerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.ChatInteractor
import com.example.dogownerapp.domain.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val interactor: ChatInteractor)
    : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

     fun loadMessages(personId: String, owner: Boolean) {
         viewModelScope.launch {
             interactor.getMessages(owner, personId).collect { mesList ->
                 _messages.value = mesList
             }
         }
    }

    fun sendMessage(personId: String, text: String, owner: Boolean) {
        viewModelScope.launch {
            val message = Message(text = text, sender = true)
            interactor.sendMessage(owner, personId, message)
        }
    }
}

