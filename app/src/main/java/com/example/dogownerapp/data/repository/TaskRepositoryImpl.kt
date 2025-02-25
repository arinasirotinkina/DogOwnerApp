package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class TaskRepositoryImpl() : TaskRepository {
    override fun getTasks(day: Date): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Flow<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun removeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task, taskId: String) {
        TODO("Not yet implemented")
    }
}