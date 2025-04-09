import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.window.Dialog
import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.presentation.screen.auth.customColors
import com.example.dogownerapp.presentation.viewmodel.PlansViewModel
import java.time.LocalDate

@Composable
fun Planning(viewModel: PlansViewModel){
    CalendarView(viewModel)
}
@Composable
fun CalendarView(viewModel: PlansViewModel) {
    var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var taskIdEdited by remember { mutableStateOf("")}
    var taskEdited by remember { mutableStateOf(Task()) }
    var name by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState()

    val dates by viewModel.daysWithTasks.collectAsState()

    val months = hashMapOf(
        1 to "Январь",
        2 to "Февраль",
        3 to "Март",
        4 to "Апрель",
        5 to "Май",
        6 to "Июнь",
        7 to "Июль",
        8 to "Август",
        9 to "Сентябрь",
        10 to "Октябрь",
        11 to "Ноябрь",
        12 to "Декабрь"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Предыдущий месяц")
            }
            Text(
                text = months[currentMonth.month.value].toString() + " " + currentMonth.year,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Следующий месяц")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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

        val firstDayIndex = currentMonth.dayOfWeek.value - 1
        val daysInMonth = currentMonth.lengthOfMonth()
        val totalCells = ((firstDayIndex + daysInMonth + 6) / 7) * 7

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(totalCells) { index ->
                val dayNumber = index - firstDayIndex + 1
                if (index < firstDayIndex || dayNumber > daysInMonth) {
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
                                viewModel.getTasksonDate(date)
                                selectedDate = date

                            }
                            .clip(CircleShape)
                            .background(
                                if (isSelected) customColors.surface else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = dayNumber.toString(),
                                color = Color.Black
                            )
                            if (date in dates) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = "+ Добавить задачу",
            fontSize = 20.sp,
            modifier = Modifier.clickable { showDialog  = true }
        )
        Spacer(Modifier.height(20.dp))
        LazyColumn (Modifier.heightIn(0.dp, 3000.dp)) {
            items(tasks) { task ->
                if (task.getLocalDate() == selectedDate) TaskItem(task, viewModel) {
                    name = task.name
                    description = task.description
                    taskEdited = task
                    taskIdEdited = task.id
                    showEditDialog = true
                }
            }
        }

        if (showDialog || showEditDialog) {
            Dialog(onDismissRequest = {
                showDialog = false
                showEditDialog = false}) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = name,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White,
                            ),
                            onValueChange = { name = it },
                            label = { Text("Название") },
                            modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(
                            value = description,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                disabledContainerColor = Color.White),
                            onValueChange = { description = it },
                            label = { Text("Описание") },
                            modifier = Modifier.fillMaxWidth())

                        Row {
                            Button(onClick = {
                                showDialog = false
                                showEditDialog = false}) {
                                Text("Закрыть")
                            }
                            Button(onClick = {
                                if (name != "") {
                                    if (showDialog) {
                                        viewModel.addTask(Task("", selectedDate.toString(), name, description))
                                    } else if (showEditDialog) {
                                        taskEdited = Task(taskIdEdited, selectedDate.toString(), name, description)
                                        viewModel.updateTask(taskEdited, taskIdEdited)
                                    }
                                }
                                name = ""
                                taskEdited = Task()
                                taskIdEdited = ""
                                description = ""
                                showDialog = false
                                showEditDialog = false
                                viewModel.getTasksonDate(selectedDate)
                                Log.i("tasks", tasks.size.toString())
                                }) {
                                Text("Сохранить")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, viewModel: PlansViewModel, onUpdate: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp).clickable { onUpdate() },
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
                    text = task.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = task.description,
                    fontSize = 16.sp
                )
            }

            IconButton(onClick = { viewModel.removeTask(task.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
            }
        }
    }
}




