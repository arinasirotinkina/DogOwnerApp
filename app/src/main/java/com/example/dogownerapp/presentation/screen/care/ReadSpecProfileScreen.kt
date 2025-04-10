package com.example.dogownerapp.presentation.screen.care

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dogownerapp.presentation.ShowLocaleMapsActivity
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.screen.home.isImageExists
import com.example.dogownerapp.presentation.viewmodel.SpecsViewModel
import com.example.dogownerapp.presentation.viewmodel.UserViewModel

@Composable
fun ReadSpecProfile(viewModel: SpecsViewModel, navController: NavController,
                    specId: String, userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val user by userViewModel.user.collectAsState()
    var favourites by remember { mutableStateOf(user.favourites)}

    val spec = viewModel.getSpecById(specId)
    var avatarUrl by remember { mutableStateOf("http://arinas8t.beget.tech/photo/specs/$specId") }
    var imageExists by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current
    LaunchedEffect(avatarUrl) {
        imageExists = isImageExists(avatarUrl)
    }
    Column(Modifier.verticalScroll(scrollState)) {
        Column  {
            if (imageExists == true) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .size(160.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(Modifier.size(12.dp))
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
                fontSize = 20.sp
            )
            Spacer(Modifier.size(12.dp))

            Text(
                text = "Номер телефона: " + spec.phoneNumber,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontSize = 20.sp
            )
            Spacer(Modifier.size(8.dp))

            Text(
                text = "Email: " + spec.email,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontSize = 20.sp
            )
            Spacer(Modifier.size(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Адрес: " + spec.address,
                        //modifier = Modifier.padding(start = 8.dp),
                        color = Color.Black,
                        fontSize = 20.sp
                    )
                }

                IconButton(onClick = {
                    val intent = Intent(context, ShowLocaleMapsActivity::class.java)
                    intent.putExtra("latitude", spec.location.latitude)
                    intent.putExtra("longitude", spec.location.longitude)
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Поиск", tint = Color.Gray)
                }
            }

            Spacer(Modifier.size(8.dp))

            Text(
                text = "О себе",
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
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
                fontSize = 20.sp
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
                text = "Услуги и цены",
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(Modifier.size(8.dp))

            Text(
                text = spec.prices,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontSize = 18.sp
            )
            Spacer(Modifier.size(8.dp))

            Text(
                text = "Условия",
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Spacer(Modifier.size(8.dp))

            Text(
                text = spec.conditions,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                fontSize = 18.sp
            )
            Spacer(Modifier.size(8.dp))


            if (specId !in favourites) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        favourites += specId
                        userViewModel.updateFavs(favourites)
                    }) {
                        Icon(Icons.Outlined.FavoriteBorder,
                            contentDescription = "Нет в избранном", tint = Color.Red)
                    }
                    Text(
                        text = "  Добавить в избранное",
                        color = Color.Red,
                        fontSize = 18.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        favourites -= specId
                        userViewModel.updateFavs(favourites)
                    }) {
                        Icon(Icons.Outlined.Favorite,
                            contentDescription = "В избранном", tint = Color.Red)
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "  В избранном",
                            color = Color.Red,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(Modifier.size(12.dp))
            Button(
                onClick = {
                    navController.navigate("chat/${spec.id}/name=${Uri.encode(spec.name + " " + spec.surname)}")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("Связаться через чат")
            }

            Spacer(Modifier.size(30.dp))
        }
    }
}