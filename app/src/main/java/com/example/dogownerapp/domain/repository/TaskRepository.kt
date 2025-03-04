package com.example.dogownerapp.domain.repository

import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTaskById(taskId: String): Flow<Task>
    suspend fun addTask(task: Task)
    suspend fun removeTask(taskId: String)
    suspend fun updateTask(task: Task, taskId: String)
}