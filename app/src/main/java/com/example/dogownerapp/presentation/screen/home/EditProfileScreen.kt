package com.example.dogownerapp.presentation.screen.home


import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.presentation.viewmodel.UserViewModel

@Composable
fun EditProfile(viewModel: UserViewModel, navController: NavController) {
    val user by viewModel.user.collectAsState()
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var phoneNumber by remember { mutableStateOf(user.phoneNumber) }
    var birthDate by remember { mutableStateOf(user.birthDate) }
    var adress by remember { mutableStateOf(user.adress) }

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
        OutlinedTextField(value = adress,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            onValueChange = { adress = it }, label = { Text("Адрес") }, modifier = Modifier.fillMaxWidth())


        Button(
            onClick = {
                 val user = User(
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,

                    birthDate = birthDate,
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
