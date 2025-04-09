package com.example.dogownerapp.presentation.screen.home

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.presentation.MapsActivity
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun EditProfile(viewModel: UserViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState()
    var surname by remember { mutableStateOf(user.surname) }
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var address by remember { mutableStateOf(user.adress) }
    var location by remember { mutableStateOf(user.location) }
    val favourites by remember { mutableStateOf(user.favourites)}
    val avatarUri by remember { mutableStateOf<Uri?>(null) }
    var randomParam = System.currentTimeMillis()
    var avatarUrl by remember { mutableStateOf("http://arinas8t.beget.tech/photo/users/${user.id}?$randomParam")}
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val addressSelected = result.data?.getStringExtra("selected_address") ?: "Адрес не найден"
            val latitude = result.data?.getDoubleExtra("selected_latitude", 0.0) ?: 0.0
            val longitude = result.data?.getDoubleExtra("selected_longitude", 0.0) ?: 0.0
            address = addressSelected
            location = GeoPoint(latitude, longitude)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageExists = false
            viewModel.uploadPhoto(uri, context) {
                randomParam = System.currentTimeMillis()
                avatarUrl ="http://arinas8t.beget.tech/photo/users/${user.id}?$randomParam"
                imageExists = true
            }
        }
    }
    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (imageExists == true) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(150.dp).clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Выбрать фото",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Номер телефона") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.size(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(16.dp)
                .clickable {
                    val intent = Intent(context, MapsActivity::class.java)
                    launcher.launch(intent)
                }
        ) {
            Text(text = if (address == "-") "Добавить адрес" else address, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val user = User(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    adress = address,
                    location = location,
                    favourites = favourites
                )
                viewModel.updateUser(user)
                navController.navigate("home") {
                    popUpTo("edit_profile") { inclusive = true }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Сохранить")
        }
    }
}

suspend fun isImageExists(url: String): Boolean = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    return@withContext try {
        val request = Request.Builder().url(url).head().build()
        val response = client.newCall(request).execute()
        response.isSuccessful
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
