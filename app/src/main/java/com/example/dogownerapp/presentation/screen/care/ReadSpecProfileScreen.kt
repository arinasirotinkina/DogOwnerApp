package com.example.dogownerapp.presentation.screen.care

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.presentation.auth.AuthActivity
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.RecommendsViewModel
import com.example.dogownerapp.presentation.viewmodel.SpecsViewModel

@Composable
fun ReadSpecProfile(viewModel: SpecsViewModel, navController: NavController, specId: String) {
    val scrollState = rememberScrollState()
    val spec = viewModel.getSpecById(specId)
    Column (Modifier.verticalScroll(scrollState)) {
        Text(
            text = spec!!.name + " " + spec.surname,
            modifier = Modifier.padding(start = 8.dp),
            color = customColors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = spec.specialization,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Номер телефона: " + spec.phoneNumber,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Email: " + spec.email,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Адрес: " + spec.address,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "О себе",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.about,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Опыт работы",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.experience,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = "Опыт работы",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(8.dp))

        Text(
            text = spec.experience,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 18.sp
        )
        Spacer(Modifier.size(30.dp))

        Button(
            onClick = {
                navController.navigate("chat/${spec.id}/name=${Uri.encode(spec.name + " " + spec.surname)}")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Связаться через чат")
        }
        Spacer(Modifier.size(60.dp))


    }


}