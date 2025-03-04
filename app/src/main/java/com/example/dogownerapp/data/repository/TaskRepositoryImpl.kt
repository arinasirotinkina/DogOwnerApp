package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.Task
import com.example.dogownerapp.domain.repository.TaskRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
                         private val firestore: FirebaseFirestore,
                         private val auth: FirebaseAuth // Получаем FirebaseAuth для получения userId
) : TaskRepository {
    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    override fun getTasks(): Flow<List<Task>> = callbackFlow {
        val tasksCollection = firestore.collection("users").document(userId).collection("tasks")

        val listener = tasksCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val tasks = snapshot?.documents?.mapNotNull { it.toObject(Task::class.java) } ?: emptyList()
            trySend(tasks).isSuccess
        }

        awaitClose { listener.remove() }
    }


    override fun getTaskById(taskId: String): Flow<Task> = callbackFlow {
        val taskDocument = firestore.collection("users").document(userId).collection("tasks").document(taskId)

        val listener = taskDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val task = snapshot?.toObject(Task::class.java)
            if (task != null) {
                trySend(task).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun addTask(task: Task) {
        val taskCollection = firestore.collection("users").document(userId).collection("tasks")
        taskCollection.add(task).await()
    }

    override suspend fun removeTask(taskId: String) {
        val taskDocument = firestore.collection("users").document(userId).collection("tasks").document(taskId)
        taskDocument.delete().await()
    }

    override suspend fun updateTask(task: Task, taskId: String) {
        val taskDocument = firestore.collection("users").document(userId).collection("tasks").document(taskId)
        taskDocument.update(
            "name", task.name,
            "date", task.date,
            "description", task.description
        ).await()
    }
}