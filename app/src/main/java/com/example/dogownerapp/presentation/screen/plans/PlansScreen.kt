import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.ui.semantics.SemanticsActions.OnClick
import androidx.compose.ui.window.Dialog
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.PlansViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Planning(viewModel: PlansViewModel){
    CalendarView(viewModel)
}
@Composable
fun CalendarView(
    viewModel: PlansViewModel
    //onDateSelected: (LocalDate) -> Unit
) {
    // Состояние текущего месяца (начинается с первого числа)
    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    // Выбранная дата по умолчанию - текущая дата
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) } // Состояние для отображения диалога

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Заголовок с кнопками навигации
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущий месяц")
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Следующий месяц")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Заголовок дней недели
        Row(modifier = Modifier.fillMaxWidth()) {
            val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Определяем день недели для первого числа месяца (в нашем случае понедельник = 1)
        val firstDayIndex = currentMonth.dayOfWeek.value - 1  // если день недели начинается с 1 для понедельника
        val daysInMonth = currentMonth.lengthOfMonth()

        // Рассчитываем общее количество ячеек для календаря (с учётом пустых ячеек перед первым днём)
        val totalCells = ((firstDayIndex + daysInMonth + 6) / 7) * 7

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(totalCells) { index ->
                val dayNumber = index - firstDayIndex + 1
                if (index < firstDayIndex || dayNumber > daysInMonth) {
                    // Пустая ячейка для выравнивания сетки
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                    )
                } else {
                    val date = currentMonth.withDayOfMonth(dayNumber)
                    val isSelected = date == selectedDate
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clickable {
                                selectedDate = date
                                //onDateSelected(date)
                            }
                            .clip(CircleShape)
                            .background(
                                if (isSelected) customColors.surface else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayNumber.toString(),
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Text("+ Добавить задачу", Modifier.height(24.dp).clickable {
            showDialog = true
        })

        if (showDialog) {
            CustomDialogExample()
        }
    }
}

@Composable
fun CustomDialogExample() {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Открыть кастомный диалог")
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Кастомный диалог", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Можно добавить любые элементы")
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { showDialog = false }) {
                            Text("Закрыть")
                        }
                    }
                }
            }
        }
    }
}
