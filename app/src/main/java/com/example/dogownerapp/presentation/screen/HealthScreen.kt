package com.example.dogownerapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Gender
import com.example.dogownerapp.domain.model.Vaccination
import com.example.dogownerapp.presentation.main.health.HealthViewModel
import java.util.Date

@Composable
fun Health(viewModel: HealthViewModel){
    val newDog = Dog(
        name = "Тузик",
        breed = "Бордер колли",
        birthDate = Date(),
        gender = Gender.MALE,
        weight = 10.0,
        castration = false,
        sterilization = false,
        vaccinations = arrayListOf(),
        treatments = arrayListOf(),
    )
    viewModel.addDog(newDog)
    val dogs by viewModel.dogs.collectAsState()
    Column {
        LazyColumn {
            items(dogs) { dog ->
                DogItem(dog)
            }
        }
        Box(
            modifier = Modifier
                .size(48.dp) // Размер кнопки
                .clip(CircleShape) // Делаем круглую форму
                .background(Color.White) // Белый фон внутри
                .border(2.dp, color = customColors.primary, CircleShape) // Цветная обводка
                .clickable { }//onClick() },
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                contentDescription = "AddDog",
                tint = colorResource(R.color.primary_bright),
                modifier = Modifier.size(30.dp).align(Alignment.Center)
            )
        }
    }

}


@Composable
fun DogItem(dog: Dog) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)) // Скругленные углы
            .border(2.dp, customColors.primary, RoundedCornerShape(16.dp)) // Красная обводка
            .background(Color.White) // Фон внутри
            .padding(16.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.start_image),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Column {
                Text(
                    text = dog.name,
                    modifier = Modifier.padding(start = 8.dp),
                    color = customColors.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                Text(
                    text = dog.breed,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp

                )
                Text(
                    text = dog.birthDate.toString(),
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 20.sp
                )
            }
        }
        Text(
            text = "${dog.weight.toString()} kg",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )

    }
}
