package com.example.dogownerapp.presentation.screen.auth

import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.MainActivity
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.presentation.auth.AuthViewModel

@Composable
fun SpecialistRegistration(viewModel: AuthViewModel, navController: NavController) {
    val state by viewModel.authResult.collectAsState()
    var email by remember { mutableStateOf("") }
    val activity = LocalActivity.current
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var passwordGo by remember { mutableStateOf(true) }

    MaterialTheme(colorScheme = customColors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.start_image),
                contentDescription = "Start Image",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.size(12.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = customColors.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.size(12.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.background),
                    unfocusedContainerColor = colorResource(R.color.background),
                    disabledContainerColor = colorResource(R.color.background),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(12.dp))

            TextField(
                value = surname,
                onValueChange = { surname = it },
                label = { Text("Фамилия") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.background),
                    unfocusedContainerColor = colorResource(R.color.background),
                    disabledContainerColor = colorResource(R.color.background),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(12.dp))


            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.background),
                    unfocusedContainerColor = colorResource(R.color.background),
                    disabledContainerColor = colorResource(R.color.background),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(12.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.background),
                    unfocusedContainerColor = colorResource(R.color.background),
                    disabledContainerColor = colorResource(R.color.background),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(12.dp))

            TextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Повторите пароль") },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.background),
                    unfocusedContainerColor = colorResource(R.color.background),
                    disabledContainerColor = colorResource(R.color.background),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(12.dp))

            Button(

                onClick = {
                    if (password == repeatPassword) {
                        viewModel.registerSpecialist(name, surname, email, password)
                    } else {
                        passwordGo = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            Spacer(Modifier.size(12.dp))


            if (!passwordGo) {
                ErrorMassage("Пароли не совпадают")
            }

            if (state is AuthResult.Error) {
                ErrorMassage((state as AuthResult.Error).message)

            }

            Text(
                text = stringResource(R.string.welcome_login),
                color = customColors.onSecondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("login") {
                            popUpTo("register") {inclusive = true}
                        }
                    }
            )

            Spacer(Modifier.size(30.dp))

            Text(
                text = "Зарегистрироваться как владелец собаки",
                color = customColors.primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("register") {
                            popUpTo("register_specialist") {inclusive = true}
                        }
                    }
            )

            if (state is AuthResult.Success) {
                val intent = Intent(activity, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }

        }
    }

}




