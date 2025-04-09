package com.example.dogownerapp.presentation.screen.care

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogownerapp.domain.model.Message
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, navController: NavController, chatId: String, name: String, owner:Boolean) {
    viewModel.loadMessages(chatId, owner)
    val messages by viewModel.messages.collectAsState()
    var inputText by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = name, style = MaterialTheme.typography.headlineMedium)

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение...") }
            )
            Button(
                onClick = {
                    if (inputText.text.isNotBlank()) {
                        viewModel.sendMessage(chatId, inputText.text, owner)
                        viewModel.loadMessages(chatId, owner)
                        inputText = TextFieldValue("")
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Отправить")
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            .padding(start = if (message.sender) 30.dp else 0.dp)
            .padding(end =  if (message.sender) 0.dp else 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (message.sender) Color.LightGray
            else Color.White
        )
    ) {
        Text(
            text = message.text,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

