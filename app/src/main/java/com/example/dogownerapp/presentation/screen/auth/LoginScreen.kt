package com.example.dogownerapp.presentation.screen.auth
import android.content.Intent
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun LoginScreen( viewModel: AuthViewModel, navController: NavController) {
    val state by viewModel.authResult.collectAsState()
    var email by remember { mutableStateOf("") }
    val activity = LocalActivity.current
    var password by remember { mutableStateOf("") }
    var context = LocalContext.current
    MaterialTheme(colorScheme = customColors) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.start_image),
                contentDescription = "Start picture",
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Space()

            Text(
                text = stringResource(R.string.app_name),
                color = colorResource(R.color.primary_bright),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Space()

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

            Space()

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль")},
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

            Space()

            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Войти")
            }

            Space()

            Text(
                text = stringResource(R.string.welcome_register),
                color = colorResource(R.color.black),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("register") {
                            popUpTo("login") {inclusive = true}
                        }
                    }
            )

            Space()

            if (state is AuthResult.Error) {
                showToast(context, (state as AuthResult.Error).message)
            }
            if (state is AuthResult.Success) {
                val intent = Intent(activity, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }

        }
    }
}

val customColors = lightColorScheme(
    primary = Color(0xFFA74B0F),
    secondary = Color(0xFFBE4D20),
    background = Color(0xFFFDF0E9),
    surface = Color(0xFFFACFB2),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color(0xFFF5BC96)
)
