package com.example.dogownerapp.presentation.viewmodel

import androidx.compose.material3.rememberTopAppBarState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogownerapp.domain.interactor.DogListInteractor
import com.example.dogownerapp.domain.interactor.TaskInteractor
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PlansViewModel @Inject constructor(
    private val tasksInteractor: TaskInteractor
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _tasksForDay = MutableStateFlow<List<Task>>(emptyList())
    val tasksForDay: StateFlow<List<Task>> = _tasksForDay.asStateFlow()

    private val _daysWithTasks = MutableStateFlow<List<LocalDate>>(emptyList())
    val daysWithTasks: StateFlow<List<LocalDate>> = _daysWithTasks.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            tasksInteractor.loadTasks().collect { taskList ->
                _tasks.value = taskList
                _daysWithTasks.value = taskList.map { it.getLocalDate() }
            }
        }
    }

    fun getTasksonDate(date: LocalDate) {
        _tasksForDay.value = _tasks.value.filter { it.getLocalDate().isEqual(date) }
    }


    fun addTask(task: Task) {
        viewModelScope.launch {
            tasksInteractor.addTask(task)
        }
        getTasksonDate(task.getLocalDate())
    }

    fun removeTask(taskId: String) {
        viewModelScope.launch {
            tasksInteractor.removeTask(taskId)
        }
    }

    fun updateTask(task: Task, taskId: String) {
        viewModelScope.launch {
            tasksInteractor.updateTask(task, taskId)
        }
    }
}