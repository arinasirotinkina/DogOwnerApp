package com.example.dogownerapp.data.datasource

import com.example.dogownerapp.domain.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun login(email: String, password: String): Flow<AuthResult> = callbackFlow {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                trySend(AuthResult.Success).isSuccess

            }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun register(email: String, password: String): Flow<AuthResult> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val userInfo = mapOf(
                        "email" to email
                    )

                    // Записываем в Firestore
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userInfo)
                        .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
                        .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Firestore error")) }
                } else {
                    trySend(AuthResult.Error("Registration successful, but user is null"))
                }
            }
            //.addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun logout() {
        firebaseAuth.signOut()
    }
}
