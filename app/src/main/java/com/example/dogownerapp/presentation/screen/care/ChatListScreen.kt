package com.example.dogownerapp.presentation.screen.care

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogownerapp.domain.model.Chat
import com.example.dogownerapp.presentation.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(chatListViewModel: ChatListViewModel, navController: NavController, owner: Boolean) {
    val scrollState = rememberScrollState()
    chatListViewModel.loadChats(owner)
    val chats by chatListViewModel.chats.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Список чатов", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(chats) { chat ->
                ChatItem(chat, navController, owner)
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, navController: NavController, owner: Boolean) {
    val chatId = chat.id
    val name = chat.name
    Log.i("name", name)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = {
                navController.navigate("chat/$chatId/name=${Uri.encode(name)}")
            }),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = chat.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
