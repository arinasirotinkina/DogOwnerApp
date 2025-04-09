package com.example.dogownerapp.presentation.screen.specialist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.presentation.MapsActivity
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.home.isImageExists
import com.example.dogownerapp.presentation.viewmodel.specialists.ProfileViewModel
import com.google.firebase.firestore.GeoPoint

@Composable
fun EditProfileScreen(profileViewModel: ProfileViewModel, navController: NavController) {
    val spec by profileViewModel.spec.collectAsState()
    var id by remember { mutableStateOf(spec.id) }
    var name by remember { mutableStateOf(spec.name) }
    var surname by remember { mutableStateOf(spec.surname) }
    var email by remember { mutableStateOf(spec.email) }
    var phoneNumber by remember { mutableStateOf(spec.phoneNumber) }
    var address by remember { mutableStateOf(spec.address) }
    var specialization by remember { mutableStateOf(spec.specialization) }
    var about by remember { mutableStateOf(spec.about) }
    var conditions by remember { mutableStateOf(spec.conditions) }
    var experience by remember { mutableStateOf(spec.experience) }
    val scrollState = rememberScrollState()
    var location by remember { mutableStateOf(spec.location) }
    var prices by remember { mutableStateOf(spec.prices) }
    var randomParam = System.currentTimeMillis()
    var avatarUrl by remember { mutableStateOf("http://arinas8t.beget.tech/photo/specs/${spec.id}?$randomParam")}
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageExists = false
            profileViewModel.uploadPhoto(uri, context) {
                randomParam = System.currentTimeMillis()
                avatarUrl ="http://arinas8t.beget.tech/photo/specs/${spec.id}?$randomParam"
                imageExists = true
            }
        }
    }
    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable { galleryLauncher.launch("image/*") },
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
            value = name,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
            ),
            onValueChange = { name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = surname,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { surname = it },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = email,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { email = it },
            label = { Text("email") },
            modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = phoneNumber,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { phoneNumber = it }, label = { Text("Номер телефона") }, modifier = Modifier.fillMaxWidth())

        val context = LocalContext.current

        Spacer(Modifier.size(8.dp))
        Column (
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
            if (address == "-" || address == "") {
                Text(
                    text = "Добавить адрес",
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
            } else {
                Text(
                    text = address,
                    fontSize = 16.sp
                )
            }


        }

        Column {
            Text(text = "Специализация: ", style = MaterialTheme.typography.bodyMedium, fontSize = 12.sp)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = ("Догситтер" == specialization),
                    onClick = { specialization = "Догситтер" }
                )
                Text(text = "Догситтер")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = ("Грумер" == specialization),
                    onClick = { specialization = "Грумер" }
                )
                Text(text = "Грумер")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = ("Ветеринар" == specialization),
                    onClick = { specialization = "Ветеринар" }
                )
                Text(text = "Ветеринар")
            }


        }

        OutlinedTextField(value = about,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { about = it }, label = { Text("О себе") },
            modifier = Modifier.fillMaxWidth().wrapContentHeight())

        OutlinedTextField(value = experience,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { experience = it }, label = { Text("Опыт работы") },
            modifier = Modifier.fillMaxWidth().wrapContentHeight())


        OutlinedTextField(value = prices,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { prices = it }, label = { Text("Услуги и цены") },
            modifier = Modifier.fillMaxWidth().wrapContentHeight())



        OutlinedTextField(value = conditions,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { conditions = it }, label = { Text("Условия приема (дома, в салоне, в клинике)") },
            modifier = Modifier.fillMaxWidth().wrapContentHeight())


        Button(
            onClick = {
                val spec = Specialist(id, name, surname, email, phoneNumber,
                    specialization, address, about, experience, conditions, prices, location)
                profileViewModel.updateSpec(spec)

                navController.navigate("profile") {
                    popUpTo("edit_profile") { inclusive = true }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Сохранить")
        }
    }

}