package com.example.dogownerapp

import SubMain
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.auth.AuthViewModel
import com.example.dogownerapp.presentation.viewmodel.ChatListViewModel
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel
import com.example.dogownerapp.presentation.viewmodel.EditDogViewModel
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel
import com.example.dogownerapp.presentation.viewmodel.PlansViewModel
import com.example.dogownerapp.presentation.viewmodel.RecommendsViewModel
import com.example.dogownerapp.presentation.viewmodel.SpecsViewModel
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.example.dogownerapp.presentation.viewmodel.specialists.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val healthViewModel: HealthViewModel by viewModels()
    private val editDogViewModel: EditDogViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val plansViewModel: PlansViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val recsViewModel: RecommendsViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val specsViewModel: SpecsViewModel by viewModels()
    private val chatListViewModel: ChatListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!authViewModel.isAuthorized()) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            runBlocking {
                setContent {
                    SubMain(healthViewModel, editDogViewModel, userViewModel,
                        plansViewModel, chatViewModel, recsViewModel,
                        profileViewModel, authViewModel, specsViewModel,
                        chatListViewModel)
                }
            }

        }
    }
}

