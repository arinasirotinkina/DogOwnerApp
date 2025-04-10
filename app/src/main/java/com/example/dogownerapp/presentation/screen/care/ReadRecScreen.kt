package com.example.dogownerapp.presentation.screen.care

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.RecommendsViewModel

@Composable
fun ReadRec(viewModel: RecommendsViewModel, recId: String) {
    val scrollState = rememberScrollState()
    val rec = viewModel.getRecById(recId)
    val tags = buildText(rec!!)
    Column(Modifier.verticalScroll(scrollState)) {
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

        Text(
            text = rec.text,
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