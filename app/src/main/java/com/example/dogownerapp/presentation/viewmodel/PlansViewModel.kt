package com.example.dogownerapp.presentation.viewmodel

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
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class PlansViewModel @Inject constructor(
    private val tasksInteractor: TaskInteractor
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    /*init {
        loadTasks()
    }*/


    private fun loadTasks(day: Date) {
        viewModelScope.launch {
            tasksInteractor.loadTasks(day).collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            tasksInteractor.addTask(task)
        }
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