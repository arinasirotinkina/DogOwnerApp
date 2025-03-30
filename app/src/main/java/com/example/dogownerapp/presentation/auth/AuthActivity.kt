package com.example.dogownerapp.presentation.auth

import com.example.dogownerapp.presentation.screen.auth.RegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dogownerapp.presentation.screen.auth.LoginScreen
import com.example.dogownerapp.presentation.screen.auth.SpecialistRegistration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    //private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "register") {
                composable("register") { RegistrationScreen(viewModel, navController) }
                composable("login") { LoginScreen(viewModel, navController) }
                composable("register_specialist") { SpecialistRegistration(viewModel, navController) }
            }
        }
    }
}

