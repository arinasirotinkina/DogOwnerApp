package com.example.dogownerapp.presentation.auth

import RegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.data.repository.AuthRepositoryImpl
import com.example.dogownerapp.databinding.ActivityRegisterBinding
import com.example.dogownerapp.domain.usecase.auth.RegistrationUseCase
import com.example.dogownerapp.presentation.factory.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dogownerapp.domain.usecase.auth.LogOutUseCase
import com.example.dogownerapp.domain.usecase.auth.LoginUseCase
import com.example.dogownerapp.presentation.screen.LoginScreen
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "register") {
                composable("register") { RegistrationScreen(viewModel, navController) }
                composable("login") { LoginScreen(viewModel, navController) }
            }
        }
    }
}

