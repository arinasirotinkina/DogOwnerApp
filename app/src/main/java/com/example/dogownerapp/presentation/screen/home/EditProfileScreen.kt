package com.example.dogownerapp.presentation.screen.home


import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.presentation.MapsActivity
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.google.firebase.firestore.GeoPoint

@Composable
fun EditProfile(viewModel: UserViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState()
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var birthDate by remember { mutableStateOf(user.birthDate) }
    var address by remember { mutableStateOf(user.adress) }
    var location by remember { mutableStateOf(user.location) }

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
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Выбрать фото", tint = Color.White)
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
            value = email,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { email = it },
            label = { Text("email") },
            modifier = Modifier.fillMaxWidth())

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
            if (address == "-") {
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
        OutlinedTextField(
            value = birthDate,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(value = phoneNumber,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { phoneNumber = it }, label = { Text("Номер телефона") }, modifier = Modifier.fillMaxWidth())


        //Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                 val user = User(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                     adress = address,
                    birthDate = birthDate,
                     location = location
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
