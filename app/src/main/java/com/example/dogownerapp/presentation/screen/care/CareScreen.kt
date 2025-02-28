package com.example.dogownerapp.presentation.screen.care

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.screen.auth.Space
import com.example.dogownerapp.presentation.screen.auth.customColors

@Composable
fun Care(){
    Column {
        CareItem("Чаты")
        CareItem("Грумеры")
        CareItem("Ветеринары")
        CareItem("Догситтеры")
        CareItem("Рекомендации")

    }

}

@Composable
fun CareItem(textVal: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Скругленные углы
            .border(1.dp, customColors.primary, RoundedCornerShape(16.dp)) // Красная обводка
            .background(Color.White) // Фон внутри
            .padding(16.dp)
            .clickable {
                //navController.navigate("edit_profile")
            }
    ) {
        Text(
            text = textVal,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )


    }

}