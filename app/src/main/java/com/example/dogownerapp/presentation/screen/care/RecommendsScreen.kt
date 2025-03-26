package com.example.dogownerapp.presentation.screen.care

import android.util.Log
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.dogownerapp.R
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.HealthViewModel
import com.example.dogownerapp.presentation.viewmodel.RecommendsViewModel
import kotlin.math.min


@Composable
fun Recommends(viewModel: RecommendsViewModel, navController: NavController){
    val scrollState = rememberScrollState()
    val recs by viewModel.recsToShow.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var healthTag by remember { mutableStateOf(true) }
    var breedsTag by remember { mutableStateOf(true) }
    var careTag by remember { mutableStateOf(true) }
    var nutritionTag by remember { mutableStateOf(true) }
    var educationTag by remember { mutableStateOf(true) }

    //Log.i("Recs", recs.size.toString())
    Column (Modifier.verticalScroll(scrollState)) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
            showDialog = true
        }) {
            Text("Фильтровать по тегам")

        }
        LazyColumn (Modifier.heightIn(0.dp, 3000.dp)) {
            items(recs) { rec ->
                RecItem(rec, navController)
            }
        }
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = healthTag, onCheckedChange = { healthTag = it })
                        Text("Здоровье")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = breedsTag, onCheckedChange = { breedsTag = it })
                        Text("Породы")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = careTag, onCheckedChange = { careTag = it })
                        Text("Уход")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = nutritionTag, onCheckedChange = { nutritionTag = it })
                        Text("Питание")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = educationTag, onCheckedChange = { educationTag = it })
                        Text("Воспитание")
                    }
                    Button(onClick = {
                        viewModel.filterByTags(healthTag, breedsTag, careTag, nutritionTag, educationTag)
                        Log.i("Tags", recs.size.toString())
                        showDialog = false
                    }) {
                        Text("Фильтровать")
                    }
                }
            }
        }
    }

}

fun buildText(rec: Recommendation): String {
    val lines = mutableListOf<String>()
    if (rec.health) {
        lines.add("Здоровье")
    }
    if (rec.breeds) {
        lines.add("Породы")
    }
    if (rec.care) {
        lines.add("Уход")
    }
    if (rec.nutrition) {
        lines.add("Питание")
    }
    if (rec.education) {
        lines.add("Воспитание")
    }

    return lines.joinToString(", ")
}

@Composable
fun RecItem(rec: Recommendation, navController: NavController) {
    val recId = rec.id
    val tags = buildText(rec)
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, customColors.primary, RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable {
                navController.navigate("read_rec/$recId")
            }
    ) {
        Text(
            text = rec.title,
            modifier = Modifier.padding(start = 8.dp),
            color = customColors.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = tags,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = rec.text.substring(0, minOf(rec.text.length, 300)) + "...",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Black,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Автор: " + rec.author,
            modifier = Modifier.padding(start = 8.dp),
            color = Color.Gray,
            fontSize = 16.sp
        )

    }
}
