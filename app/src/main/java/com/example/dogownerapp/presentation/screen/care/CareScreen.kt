package com.example.dogownerapp.presentation.screen.care

import NavRoutes
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.R
import com.example.dogownerapp.presentation.MapsActivity
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.screen.auth.Space
import com.example.dogownerapp.presentation.screen.auth.customColors

@Composable
fun Care(navController: NavController){
    var selectedAddress by remember { mutableStateOf("Выберите адрес") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val address = result.data?.getStringExtra("selected_address") ?: "Адрес не найден"
            selectedAddress = address
        }
    }
    Column {
        CareItem("Чаты", navController,"chat_list")
        CareItem("Грумеры", navController,NavRoutes.Specs.route)
        CareItem("Ветеринары",navController, "veterinary")
        CareItem("Догситтеры", navController,"veterinary")
        CareItem("Рекомендации", navController,"recs")
        val context = LocalContext.current
        Button(onClick = {
            val intent = Intent(context, MapsActivity::class.java)
            launcher.launch(intent)
        }) {
            Text("Открыть фрагмент")
        }
        Text(selectedAddress)
    }

}

@Composable
fun CareItem(textVal: String, navController: NavController, route: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Скругленные углы
            .border(1.dp, customColors.primary, RoundedCornerShape(16.dp)) // Красная обводка
            .background(Color.White) // Фон внутри
            .padding(16.dp)
            .clickable {
                navController.navigate(route)
            }
    ) {
        Text(
            text = textVal,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )


    }

}