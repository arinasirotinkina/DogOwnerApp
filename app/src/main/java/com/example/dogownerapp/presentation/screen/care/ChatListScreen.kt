package com.example.dogownerapp.presentation.screen.care

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Chat(val id: String, val name: String)

val sampleChats = listOf(
    Chat("1", "Саша"),
    Chat("2", "Марина"),
    Chat("3", "Дмитрий"),
    Chat("4", "Ольга")
)

@Composable
fun ChatListScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Список чатов", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(sampleChats) { chat ->
                ChatItem(chat) {
                    navController.navigate("chat/${chat.id}")
                }
            }
        }
    }
}

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = chat.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun PreviewChatsScreen() {
    ChatListScreen(navController = androidx.navigation.compose.rememberNavController())
}
