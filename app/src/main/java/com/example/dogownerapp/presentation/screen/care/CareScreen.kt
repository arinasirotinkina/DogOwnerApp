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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val scrollState = rememberScrollState()
    Column(Modifier.verticalScroll(scrollState)) {
        CareItem("Чаты",
            "Ваши диалоги со специалистами: узнайте " +
                    "подробности и договоритесь о приеме",
            navController,"chat_list")
        CareItem("Грумеры",
            "Поиск специалистов по уходу и стрижке",
            navController,NavRoutes.Groomers.route)
        CareItem("Ветеринары",
            "Поиск врача для собаки",
            navController, NavRoutes.Vets.route)
        CareItem("Догситтеры",
            "Поиск специалиста по передержке и воспитанию собаки",
            navController,NavRoutes.Dogsitters.route)
        CareItem("Избранное",
            "Специалисты, которые вам понравились",
            navController,NavRoutes.Favourites.route)

        CareItem("Рекомендации",
            "Статьи об уходе за питомцем, здоровье, питании и воспитании",
            navController,"recs")
    }
}

@Composable
fun CareItem(textVal: String, textDesc: String, navController: NavController, route: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(7.dp).height(112.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, customColors.primary, RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable {
                navController.navigate(route)
            }
    ) {
        Text(
            text = textVal,
            modifier = Modifier.padding(start = 8.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = textDesc,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Gray,
            fontSize = 18.sp
        )
    }
}