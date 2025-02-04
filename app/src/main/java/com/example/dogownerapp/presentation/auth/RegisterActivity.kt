package com.example.dogownerapp.presentation.auth

import RegistrationScreen
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dogownerapp.R
import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.data.repository.AuthRepositoryImpl
import com.example.dogownerapp.databinding.ActivityMainBinding
import com.example.dogownerapp.databinding.ActivityRegisterBinding
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.repository.AuthRepository
import com.example.dogownerapp.domain.usecase.auth.RegisterUser
import com.example.dogownerapp.presentation.factory.AuthViewModelFactory
import com.example.dogownerapp.presentation.ui.health.HealthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost


class RegisterActivity : ComponentActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory(
            RegisterUser(AuthRepositoryImpl(FirebaseAuthDataSource(FirebaseAuth.getInstance()))),
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Устанавливаем UI через Jetpack Compose
        setContent {
            RegistrationScreen(viewModel = viewModel)
        }
        /*
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.registerButton.setOnClickListener {
            viewModel.register("hdddjj@mail.ru", "sjjd46jdj")
        }
        val email = "test@example.com"
        val password = "password123"

        viewModel.register(email, password)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authResult.collectLatest { authResult ->
                    when (authResult) {
                        is AuthResult.Success -> {
                            //Toast.makeText(, "Login Successful", Toast.LENGTH_SHORT).show()
                            Log.i("AUTHINFO", "+")
                        }
                        is AuthResult.Error -> {
                            //Toast.makeText(requireContext(), "Error: ${authResult.message}", Toast.LENGTH_SHORT).show()
                            Log.i("AUTHINFO", "-")
                        }
                        null -> {}
                    }
                }
            }
        }*/
    }


}
