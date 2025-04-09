package com.example.dogownerapp.presentation.screen.care

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.ChatViewModel
import com.example.dogownerapp.presentation.viewmodel.SpecsViewModel
import com.example.dogownerapp.presentation.viewmodel.UserViewModel

@Composable
fun SearchSpec(viewModel: SpecsViewModel,
               specialization : String,
               navController: NavController,
               userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    var closerState by remember { mutableStateOf(false) }
    val user by userViewModel.user.collectAsState()
    if (specialization == "") {
        viewModel.showFavourites(user.location, user.favourites)
    } else {
        viewModel.filterBySpecialization(user.location, specialization)
    }
    val specs by viewModel.specsToShow.collectAsState()
    Column (Modifier.verticalScroll(scrollState)) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = closerState, onCheckedChange = { closerState = it })
            Text("Рядом со мной")
        }
        LazyColumn (Modifier.heightIn(0.dp, 3000.dp)) {
            items(specs) { spec ->
                SpecItem(spec, navController)
            }
        }
    }
    if (closerState) {
        viewModel.findCloserSpecs(user.location)
    }
}


@Composable
fun SpecItem(spec: Specialist, navController: NavController) {
    val specId = spec.id
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, customColors.primary, RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable {
                navController.navigate("read_spec/$specId")
            }
    ) {
        Text(
            text = spec.name + " " + spec.surname,
            modifier = Modifier.padding(start = 8.dp),
            color = customColors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = spec.address,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Gray,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = spec.about.substring(0, minOf(spec.about.length, 100)) + "...",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = spec.prices.substring(0, minOf(spec.prices.length, 100)) + "...",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(12.dp))
    }
}
