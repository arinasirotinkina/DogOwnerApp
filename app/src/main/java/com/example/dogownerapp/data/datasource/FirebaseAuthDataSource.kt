package com.example.dogownerapp.data.datasource

import com.example.dogownerapp.domain.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    fun login(email: String, password: String): Flow<AuthResult> = callbackFlow {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun register(email: String, password: String): Flow<AuthResult> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
