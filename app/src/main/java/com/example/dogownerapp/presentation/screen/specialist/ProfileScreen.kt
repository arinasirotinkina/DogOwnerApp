package com.example.dogownerapp.presentation.screen.specialist

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.R
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.auth.AuthViewModel
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.home.isImageExists
import com.example.dogownerapp.presentation.viewmodel.specialists.ProfileViewModel

@Composable
fun Profile(authViewModel: AuthViewModel, profileViewModel: ProfileViewModel, navController: NavController) {
    val scrollState = rememberScrollState()
    val spec by profileViewModel.spec.collectAsState()
    val context = LocalContext.current
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val randomParam by remember { mutableStateOf(System.currentTimeMillis().toString()) }
    val avatarUrl = "http://arinas8t.beget.tech/photo/specs/${spec.id}?$randomParam"

    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }

    Column (Modifier.verticalScroll(scrollState)) {

        Row(verticalAlignment = Alignment.CenterVertically ) {
            if (imageExists == true) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(Modifier.size(6.dp))
            Column {
                Text(
                    text = spec.name,
                    modifier = Modifier.padding(start = 8.dp),
                    color = customColors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
                Text(
                    text = spec.surname,
                    modifier = Modifier.padding(start = 8.dp),
                    color = customColors.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp

                )
                Text(
                    text = spec.specialization,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp
                )
            }
        }


        Spacer(Modifier.size(20.dp))

        Text(
            text = "Номер телефона: " + spec.phoneNumber,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Email: " + spec.email,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Адрес: " + spec.address,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "О себе",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.about,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Опыт работы",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.experience,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Услуги и цены",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.prices,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Условия",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = spec.conditions,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(60.dp))

        Text(
            text = "Редактировать профиль",
            modifier = Modifier.padding(start = 8.dp).clickable {
                navController.navigate("edit_profile") {
                    popUpTo("profile") { inclusive = true }
                }
            },
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(16.dp))

        Text(
            text = "Выйти",
            modifier = Modifier.padding(start = 8.dp).clickable {
                authViewModel.logout()
                val intent = Intent(context, AuthActivity::class.java)
                context.startActivity(intent)
                if (context is ComponentActivity) {
                    context.finish()
                }
            },
            color = Color.Red,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(8.dp))
    }

}