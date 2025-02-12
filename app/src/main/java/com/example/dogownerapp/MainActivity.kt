package com.example.dogownerapp

import Main
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.dogownerapp.databinding.ActivityMainBinding
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.ui.CustomTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) { // Если пользователь не авторизован
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish() // Закрываем MainActivity, чтобы не вернуться назад

        } else {
            runBlocking {
                setContent {
                    CustomTheme {
                        Main()
                    }
                }
                /*val navView: BottomNavigationView = binding.navView

                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)*/
            }
        }

    }
}