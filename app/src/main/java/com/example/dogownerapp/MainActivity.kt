package com.example.dogownerapp

import SubMain
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.auth.AuthViewModel
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
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

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
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!authViewModel.isAuthorized()) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE)
            }
            runBlocking {
                setContent {
                    SubMain(healthViewModel, editDogViewModel, userViewModel,
                        plansViewModel, chatViewModel, recsViewModel,
                        profileViewModel, authViewModel, specsViewModel)
                }
            }

        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

