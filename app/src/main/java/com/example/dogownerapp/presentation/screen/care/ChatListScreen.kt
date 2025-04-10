package com.example.dogownerapp.presentation.screen.care

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.domain.model.Chat
import com.example.dogownerapp.presentation.screen.auth.Space
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel

@Composable
fun ChatListScreen(chatViewModel: ChatViewModel, navController: NavController, owner: Boolean) {
    chatViewModel.loadChats(owner)
    val chats by chatViewModel.chats.collectAsState()
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Список чатов", fontSize = 24.sp, fontWeight = FontWeight.Bold,
            color = customColors.primary)
        Space()
        LazyColumn {
            items(chats) { chat ->
                ChatItem(chat, navController)
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, navController: NavController) {
    val chatId = chat.id
    val name = chat.name
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                .background(Color.White)
                .clickable(onClick = {
                    navController.navigate("chat/$chatId/name=${Uri.encode(name)}")
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = chat.name,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
