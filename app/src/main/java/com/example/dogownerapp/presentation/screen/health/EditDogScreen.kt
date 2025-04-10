package com.example.dogownerapp.presentation.screen.health

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Gender
import com.example.dogownerapp.presentation.screen.home.isImageExists
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
    var randomParam = System.currentTimeMillis()
    var dogIdNew by remember { mutableStateOf("") }
    if (dogId != null) {
        dogIdNew = dogId
    }
    var avatarUrl by remember { mutableStateOf("http://arinas8t.beget.tech/photo/dogs/$dogIdNew?$randomParam")}
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageExists = false
            dogIdNew = dogId ?: System.currentTimeMillis().toString()
            viewModel.uploadPhoto(uri, context, dogIdNew) {
                randomParam = System.currentTimeMillis()
                avatarUrl = "http://arinas8t.beget.tech/photo/dogs/${dogIdNew}?$randomParam"
                imageExists = true
            }
        }
    }
    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }

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
                .clickable {

                    galleryLauncher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {
            if (imageExists == true) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Выбрать фото",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White),
            value = name, onValueChange = { name = it }, label = { Text("Кличка") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White),
            value = breed, onValueChange = { breed = it }, label = { Text("Порода") }, modifier = Modifier.fillMaxWidth())

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
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Дата рождения (ДД.ММ.ГГГГ)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White),
            value = weight, onValueChange = { weight = it }, label = { Text("Вес (кг)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))


        Column {
            Text(text = "Информация о кастрации/стерилизации: ", style = MaterialTheme.typography.bodyMedium)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (isCastrated),
                    onClick = {
                        isCastrated = true
                        isSterilized = false
                    }
                )
                Text(text = "Кастрирован(а)")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (isSterilized),
                    onClick = {
                        isCastrated = false
                        isSterilized = true
                    }
                )
                Text(text = "Стерилизован(а)")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (!isCastrated && !isSterilized),
                    onClick = {
                        isCastrated = false
                        isSterilized = false
                    }
                )
                Text(text = "-")
            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White),
            value = newVaccineName, onValueChange = { newVaccineName = it }, label = { Text("Название прививки") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
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
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                            .background(Color.White).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${vaccine.name} - ${vaccine.date}",
                                fontSize = 16.sp,
                            )
                        }

                        IconButton(onClick = { viewModel.removeVaccine(vaccine) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
            value = newTreatmentName, onValueChange = { newTreatmentName = it }, label = { Text("Название обработки") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White),
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
                            //.padding(end = 24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                            .background(Color.White).padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${treatment.name} - ${treatment.date}",
                                fontSize = 16.sp,
                            )
                        }

                        IconButton(onClick = { viewModel.removeTreatment(treatment) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (dogId != null) {
                IconButton(
                    onClick = {
                        healthViewModel.removeDog(dogId)
                        navController.navigate("health") {
                            popUpTo("edit_dog") { inclusive = true }
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Удалить собаку",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (name == "") {
                        emptyName = true
                    } else {
                        val gen = if (gender == "Мальчик") Gender.MALE else Gender.FEMALE
                        val dog = Dog(
                            id = dogId ?: "0",
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
                            healthViewModel.addDog(dog, dogIdNew)
                        } else {
                            healthViewModel.updateDog(dog, dogId)
                        }
                        viewModel.clear()

                        navController.navigate("health") {
                            popUpTo("edit_dog") { inclusive = true }
                        }
                    }
                }
            ) {
                Text(text = if (dogId == null) "Добавить собаку" else "Обновить информацию")
            }
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

