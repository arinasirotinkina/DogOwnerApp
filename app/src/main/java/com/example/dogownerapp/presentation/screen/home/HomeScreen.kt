package com.example.dogownerapp.presentation.screen.home

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.auth.AuthViewModel
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.UserViewModel

@Composable
fun Home(userViewModel: UserViewModel, authViewModel: AuthViewModel, navController: NavController){
    var showDialog by remember { mutableStateOf(false) }
    val user by userViewModel.user.collectAsState()
    val context = LocalContext.current
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val randomParam by remember { mutableStateOf(System.currentTimeMillis().toString()) }
    var avatarUrl = "http://arinas8t.beget.tech/photo/users/${user.id}?$randomParam"
    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }
    Column {

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, customColors.primary, RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically ) {
                if (imageExists == true) {
                    Image(
                        painter = rememberAsyncImagePainter(avatarUrl),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(Modifier.size(6.dp))
                Column {
                    Text(
                        text = user.name,
                        modifier = Modifier.padding(start = 8.dp),
                        color = customColors.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )
                    Text(
                        text = user.surname,
                        modifier = Modifier.padding(start = 8.dp),
                        color = customColors.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp

                    )
                    Text(
                        text = user.email,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            if (user.phoneNumber != "") {
                Text(
                    text = "Номер телефона: " + user.phoneNumber,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 22.sp
                )
                Spacer(Modifier.height(8.dp))
            }
            Text(
                text = "Адрес: " + user.adress,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 22.sp
            )
            Spacer(Modifier.height(8.dp))


        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, customColors.primary, RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .clickable {
                    navController.navigate("edit_profile")
                }
        ) {
            Text(
                text = "Редактировать профиль",
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 22.sp
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, customColors.primary, RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .clickable {
                    showDialog = true
                }
        ) {
            Text(
                text = "Помощь",
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 22.sp
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, customColors.primary, RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .clickable {
                    authViewModel.logout()
                    val intent = Intent(context, AuthActivity::class.java)
                    context.startActivity(intent)
                    if (context is ComponentActivity) {
                        context.finish()
                    }
                },
        ) {
            Text(
                text = "Выйти",
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Red,
                fontSize = 22.sp
            )
        }

    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Сообщить о неполадках и задать возникшие вопросы можно " +
                            "по электронной почте разработчика: adsirotinkina@edu.hse.ru\n\n" +
                            "Спасибо, что выбираете DogOwnerApp!",
                        fontSize = 18.sp)
                    Spacer(Modifier.size(8.dp))
                    Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick =  {
                        showDialog = false }) {
                        Text(text = "Закрыть")
                    }
                }
            }
        }
    }

}