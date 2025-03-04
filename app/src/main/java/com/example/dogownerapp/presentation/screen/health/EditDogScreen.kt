package com.example.dogownerapp.presentation.screen.health

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Gender
import com.example.dogownerapp.presentation.viewmodel.EditDogViewModel
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel

@Composable
fun EditDog(viewModel: EditDogViewModel, healthViewModel: HealthViewModel, navController: NavController, dogId: String?) {

    var isLoading by remember { mutableStateOf(true) }

    val dog by viewModel.dog.collectAsState()

    LaunchedEffect(dogId) {
        if (dogId != null) {
            viewModel.getDogbyId(dogId)
        }
    }
    if (dogId != null && dog.name.isBlank()) {
        CircularProgressIndicator()
        return
    }

    var emptyName by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(dog.name) }
    var breed by remember { mutableStateOf(dog.breed) }
    var gender by remember { mutableStateOf(if (dog.gender == Gender.MALE) "Мальчик" else "Девочка") }
    var birthDate by remember { mutableStateOf(dog.birthDate) }
    var weight by remember { mutableStateOf(dog.weight.toString()) }
    var isSterilized by remember { mutableStateOf(dog.sterilization) }
    var isCastrated by remember { mutableStateOf(dog.castration) }
    var newVaccineName by remember { mutableStateOf("") }
    var newVaccineDate by remember { mutableStateOf("") }
    var newTreatmentName by remember { mutableStateOf("") }
    var newTreatmentDate by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val vaccineList by viewModel.vaccines.collectAsState()
    val treatList by viewModel.treatments.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {  },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Выбрать фото", tint = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Кличка") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Порода") }, modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Пол:", fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
            listOf("Мальчик", "Девочка").forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { gender = it }
                ) {
                    RadioButton(selected = gender == it, onClick = { gender = it })
                    Text(it, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Вес (кг)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))


        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isSterilized, onCheckedChange = { isSterilized = it })
            Text("Стерилизация")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isCastrated, onCheckedChange = { isCastrated = it })
            Text("Кастрация")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = newVaccineName, onValueChange = { newVaccineName = it }, label = { Text("Название прививки") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = newVaccineDate,
            onValueChange = { newVaccineDate = it },
            label = { Text("Дата прививки (ДД.ММ.ГГГГ)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (newVaccineName.isNotBlank() && newVaccineDate.isNotBlank()) {
                    viewModel.addVaccine(newVaccineName, newVaccineDate)
                    newVaccineName = ""
                    newVaccineDate = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить прививку")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.heightIn(min = 0.dp, max = 1600.dp)
        ) {
            items(vaccineList.reversed()) { vaccine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${vaccine.name} - ${vaccine.date}")
                        IconButton(onClick = { viewModel.removeVaccine(vaccine) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = newTreatmentName, onValueChange = { newTreatmentName = it }, label = { Text("Название обработки") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            value = newTreatmentDate,
            onValueChange = { newTreatmentDate = it },
            label = { Text("Дата обработки (ДД.ММ.ГГГГ)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                if (newTreatmentName.isNotBlank() && newTreatmentDate.isNotBlank()) {
                    viewModel.addTreatment(newTreatmentName, newTreatmentDate)
                    newTreatmentName = ""
                    newTreatmentDate = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить обработку")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.heightIn(min = 0.dp, max = 1600.dp)
        ) {
            items(treatList.reversed()) { treatment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("${treatment.name} - ${treatment.date}")
                        IconButton(onClick = { viewModel.removeTreatment(treatment) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (name == "") {
                    emptyName = true
                }  else {

                    val gen = if (gender == "Мальчик") Gender.MALE else Gender.FEMALE
                    val dog = Dog(
                        id = dogId ?: "0", // Новый id для добавления, если dogId == null
                        name = name,
                        breed = breed,
                        birthDate = birthDate,
                        gender = gen,
                        weight = weight.toDouble(),
                        castration = isCastrated,
                        sterilization = isSterilized,
                        vaccinations = vaccineList,
                        treatments = treatList
                    )

                    if (dogId == null) {
                        healthViewModel.addDog(dog)
                    } else {
                        healthViewModel.updateDog(dog, dogId) // Новый метод для обновления информации
                    }
                    viewModel.clear()

                    navController.navigate("health") {
                        popUpTo("edit_dog") { inclusive = true }
                    }
                }

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (dogId == null) "Добавить собаку" else "Обновить информацию")
        }
        if (emptyName) {
            Text("Введите кличку собаки!", color = Color.Red)
        }
    }
    BackHandler {
        viewModel.clear()
        navController.popBackStack()
    }
}
