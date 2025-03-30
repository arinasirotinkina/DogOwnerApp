package com.example.dogownerapp.data.datasource

import android.util.Log
import com.example.dogownerapp.domain.model.AuthResult
import com.example.dogownerapp.domain.model.Dog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
                        "role" to "owner",
                        "email" to email
                    )
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userInfo)
                        .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
                        .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Firestore error")) }
                } else {
                    trySend(AuthResult.Error("Registration successful, but user is null"))
                }
            }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun registerSpecialist(name: String, surname: String, email: String, password: String):
            Flow<AuthResult> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val userInfo = mapOf(
                        "role" to "specialist",
                    )
                    val specialistInfo = mapOf(
                        "email" to email,
                        "name" to name,
                        "surname" to surname
                    )
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userInfo)
                        .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
                        .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Firestore error")) }
                    firestore.collection("specialists")
                        .document(user.uid)
                        .set(specialistInfo)
                        .addOnSuccessListener { trySend(AuthResult.Success).isSuccess }
                        .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Firestore error")) }
                } else {
                    trySend(AuthResult.Error("Registration successful, but user is null"))
                }
            }
            .addOnFailureListener { trySend(AuthResult.Error(it.message ?: "Unknown error")) }

        awaitClose { }
    }

    fun isAuthorized() : Boolean {
        if (firebaseAuth.currentUser== null) {
            Log.i("auth", "not")
        }

        return firebaseAuth.currentUser != null
    }

    fun isOwner(): Flow<Boolean> = callbackFlow {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val userInfo = firestore.collection("users").document(user.uid)
            val listener = userInfo.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                Log.i("hjgjgv", "gdjhg")
                val role = snapshot?.toObject(String::class.java)
                if (role != null) {
                    trySend(true).isSuccess
                }
            }

            awaitClose { listener.remove() }
//                if (e != null) {
//                    close(e)
//                    return@addSnapshotListener
//                }
//                Log.i("roleFire", user?.uid.toString())
//                val role = snapshot?.get("role").toString()
//                if (role == "owner") {
//                    trySend(true).isSuccess
//                } else {
//                    trySend(false).isSuccess
//                }

        }

    }
//    fun isOfwner() = callbackFlow {
//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            val userId = user.uid
//            Log.i("roleFire", "f".toString())
//            val listener = firestore.collection("users").document(userId)
//                .addSnapshotListener { documentSnapshot, e ->
//                    if (e != null) {
//                        close(e)
//                        return@addSnapshotListener
//                    }
//                    if (documentSnapshot != null && documentSnapshot.exists()) {
//                        val role = documentSnapshot.getString("role")
//                        Log.i("role", role.toString())
//                        trySend(role == "owner").isSuccess
//                    } else {
//                        Log.i("roleBroken", "f".toString())
//                        trySend(false).isSuccess
//                    }
//                }
//            awaitClose { listener.remove() }
//        } else {
//            trySend(false).isSuccess
//            close()
//        }
//    }
}
