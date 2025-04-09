package com.example.dogownerapp.domain.interactor

import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskInteractor @Inject constructor (private val repository: TaskRepository) {
    fun loadTasks(): Flow<List<Task>> {
        return repository.getTasks()
    }

    suspend fun addTask(task: Task) {
        repository.addTask(task)
    }

    suspend fun removeTask(taskId: String) {
        repository.removeTask(taskId)
    }

    suspend fun updateTask(task: Task, taskId: String) {
        repository.updateTask(task, taskId)
    }

}