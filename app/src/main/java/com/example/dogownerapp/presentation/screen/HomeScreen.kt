package com.example.dogownerapp.presentation.screen

import Space
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Home(userViewModel: UserViewModel){
    var fauth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    var f = FirebaseAuthDataSource(fauth, firestore)
    val user by userViewModel.user.collectAsState()
    val context = LocalContext.current
    Column {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)) // Скругленные углы
                .border(2.dp, customColors.primary, RoundedCornerShape(16.dp)) // Красная обводка
                .background(Color.White) // Фон внутри
                .padding(16.dp)
        ) {
            Text(
                text = user.name,
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = user.email,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 24.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = user.phoneNumber,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 24.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = user.adress,
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 24.sp
            )


        }
        SettingsItem("Редактировать профиль",)
        SettingsItem("Архив")
        SettingsItem("Уведомления")
        SettingsItem("Помощь")
        //SettingsItem("Выйти")
        Space()
        Button(
            onClick = { f.logout()
                val intent = Intent(context, AuthActivity::class.java)
                context.startActivity(intent)
                if (context is ComponentActivity) {
                    // Завершаем текущую активность
                    context.finish()
                }
            },
            modifier = Modifier.fillMaxWidth().height(32.dp)
        ) {
            Text("Выйти")
        }
    }
    //Text("About Page", fontSize = 30.sp)

}

@Composable
fun SettingsItem(textVal: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Скругленные углы
            .border(1.dp, customColors.primary, RoundedCornerShape(16.dp)) // Красная обводка
            .background(Color.White) // Фон внутри
            .padding(16.dp)
    ) {
        Text(
            text = textVal,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )


    }
}
fun ii() {

}