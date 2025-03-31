package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.DogRepository
import com.example.dogownerapp.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // Получаем FirebaseAuth для получения userId
) : UserRepository {
    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    override fun getUser(): Flow<User> = callbackFlow{
        val userDocument = firestore.collection("users").document(userId)

        val listener = userDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val user = snapshot?.toObject(User::class.java)
            if (user != null) {
                trySend(user).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun updateUser(user: User) {
        val userDocument = firestore.collection("users").document(userId)
        userDocument.update(
            "name", user.name,
            "email", user.email,
            "birthDate", user.birthDate,
            "phoneNumber", user.phoneNumber,
            "adress", user.adress,
            "location", user.location
        ).await()
    }


}