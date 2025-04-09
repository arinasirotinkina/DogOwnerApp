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
            .addOnFailureListener {
                trySend(AuthResult.Error(localizeLoginError(it.message ?: "Неизвестная ошибка")))
            }
        awaitClose { }
    }

    fun register(name: String, surname: String, email: String, password: String): Flow<AuthResult> = callbackFlow {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                if (user != null) {
                    val userInfo = mapOf(
                        "role" to "owner",
                        "email" to email,
                        "name" to name,
                        "surname" to surname
                    )
                    firestore.collection("users")
                        .document(user.uid)
                        .set(userInfo)
                        .addOnSuccessListener {
                            trySend(AuthResult.Success).isSuccess
                        }
                        .addOnFailureListener {
                            trySend(AuthResult.Error(localizeError(it.message ?: "Firestore error")))
                        }
                } else {
                    trySend(AuthResult.Error("Регистрация прошла, но пользователь не найден."))
                }
            }
            .addOnFailureListener {
                trySend(AuthResult.Error(localizeError(it.message ?: "Неизвестная ошибка")))
            }
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
                        .addOnSuccessListener {
                            trySend(AuthResult.Success).isSuccess
                        }
                        .addOnFailureListener {
                            trySend(AuthResult.Error(localizeError(it.message ?: "Firestore error")))
                        }
                    firestore.collection("specialists")
                        .document(user.uid)
                        .set(specialistInfo)
                        .addOnSuccessListener {
                            trySend(AuthResult.Success).isSuccess
                        }
                        .addOnFailureListener {
                            trySend(AuthResult.Error(localizeError(it.message ?: "Firestore error")))
                        }
                } else {
                    trySend(AuthResult.Error("Регистрация прошла, но пользователь не найден."))
                }
            }
            .addOnFailureListener { trySend(AuthResult.Error(localizeError(it.message ?: "Неизвестная ошибка"))) }
        awaitClose { }
    }

    fun isAuthorized() : Boolean {
        return firebaseAuth.currentUser != null
    }

    private fun localizeError(message: String): String {
        return when {
            "email address is badly formatted" in message.lowercase() ->
                "Некорректный формат электронной почты."
            "email address is already in use" in message.lowercase() ->
                "Этот адрес электронной почты уже используется."
            "password should be at least" in message.lowercase() ->
                "Пароль слишком короткий. Минимум 6 символов."
            "network error" in message.lowercase() ->
                "Проблема с интернет-соединением."
            else -> "Ошибка: $message"
        }
    }

    private fun localizeLoginError(message: String): String {
        return when {
            "no user record corresponding" in message.lowercase() ||
                    "there is no user record" in message.lowercase() ->
                "Пользователь с таким email не найден."
            "password is invalid" in message.lowercase() ->
                "Неверный пароль. Попробуйте снова."
            "email address is badly formatted" in message.lowercase() ->
                "Некорректный формат электронной почты."
            "network error" in message.lowercase() ->
                "Ошибка сети. Проверьте подключение к интернету."
            else -> "Ошибка входа: $message"
        }
    }

}
