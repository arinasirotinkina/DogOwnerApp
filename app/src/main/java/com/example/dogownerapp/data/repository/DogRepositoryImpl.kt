package com.example.dogownerapp.data.repository

import SaveImageService
import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.dogownerapp.domain.model.Dog
import com.example.dogownerapp.domain.repository.DogRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DogRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // Получаем FirebaseAuth для получения userId
) : DogRepository {

    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    override fun getDogs(): Flow<List<Dog>> = callbackFlow {
        val dogsCollection = firestore.collection("users").document(userId).collection("dogs")

        val listener = dogsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val dogs = snapshot?.documents?.mapNotNull { it.toObject(Dog::class.java)?.copy(id = it.id) } ?: emptyList()
            trySend(dogs).isSuccess
        }

        awaitClose { listener.remove() }
    }

    override fun getDogById(dogId: String): Flow<Dog> = callbackFlow {
        val dogDocument = firestore.collection("users").document(userId).collection("dogs").document(dogId)

        val listener = dogDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val dog = snapshot?.toObject(Dog::class.java)?.copy(id = snapshot.id)
            if (dog != null) {
                trySend(dog).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun addDog(dog: Dog, dogId: String) {
        val dogsCollection = firestore.collection("users").document(userId).collection("dogs")
        var documentId: String? = null
        dogsCollection.add(dog)
            .addOnSuccessListener { documentReference ->
                documentId = documentReference.id // Получаем назначенный ID
                }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Ошибка добавления", e)
            }.await()
        if (documentId != null) {
            val imS = SaveImageService()
            imS.renameFileOnFTP("dogs", dogId, documentId!!) // Загружаем фото

        }

    }

    override suspend fun removeDog(dogId: String) {
        val dogDocument = firestore.collection("users").document(userId).collection("dogs").document(dogId)
        dogDocument.delete().await()
    }

    override suspend fun updateDog(dog: Dog, dogId: String) {
        val dogDocument = firestore.collection("users").document(userId).collection("dogs").document(dogId)
        dogDocument.update(
            "name", dog.name,
            "breed", dog.breed,
            "birthDate", dog.birthDate,
            "gender", dog.gender,
            "weight", dog.weight,
            "castration", dog.castration,
            "sterilization", dog.sterilization,
            "vaccinations", dog.vaccinations,
            "treatments", dog.treatments
        ).await()
    }
}
