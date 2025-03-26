package com.example.dogownerapp.presentation.screen.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Gender
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel

@Composable
fun Health(viewModel: HealthViewModel, navController: NavController){
    val scrollState = rememberScrollState()
    //viewModel.addDog(newDog)
    val dogs by viewModel.dogs.collectAsState()
    Column (Modifier.verticalScroll(scrollState)) {

        LazyColumn (Modifier.heightIn(0.dp, 3000.dp)) {
            items(dogs) { dog ->
                DogItem(dog, navController)
            }
        }
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, color = customColors.primary, CircleShape)
                .clickable { navController.navigate("edit_dog")}
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
fun DogItem(dog: Dog, navController: NavController) {
    val dogId = dog.id
    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, customColors.primary, RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable {
                navController.navigate("edit_dog/$dogId")
            }
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.start_image),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Column {
                Spacer(Modifier.height(16.dp))
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
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = dog.birthDate.toString(),
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )
        Text(
            text = "${dog.weight} кг",
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )
        Text(
            text = "Прививки:",
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 16.sp
        )
        Text(
            text = dog.getVaccinationsList(),
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )
        Text(
            text = "Обработки от паразитов:",
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 16.sp
        )
        Text(
            text = dog.getTreatmentsList(),
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 20.sp
        )

    }
}
