package com.example.dogownerapp.data.repository

import android.util.Log
import com.example.dogownerapp.data.datasource.FirebaseAuthDataSource
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (private val dataSource: FirebaseAuthDataSource,
    private  val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth) : AuthRepository {
    override suspend fun login(email: String, password: String): Flow<AuthResult> {
        return dataSource.login(email, password)
    }

    override suspend fun register(name: String, surname: String,
                                  email: String, password: String): Flow<AuthResult> {
        return dataSource.register(name, surname, email, password)
    }

    override suspend fun registerSpecialist(
        name: String,
        surname: String,
        email: String,
        password: String
    ): Flow<AuthResult> {
        return dataSource.registerSpecialist(name, surname, email, password)
    }

    override suspend fun logout() {
        return dataSource.logout()
    }

    override fun isAuthorized(): Boolean {
        return dataSource.isAuthorized()
    }

    override fun getUser(): Flow<User> = callbackFlow{
         val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

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
         val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val userDocument = firestore.collection("users").document(userId)
        userDocument.update(
            "name", user.name,
            "email", user.email,
            "phoneNumber", user.phoneNumber,
            "adress", user.adress
        ).await()
    }
}