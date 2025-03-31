package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.model.User
import com.example.dogownerapp.domain.repository.SpecialistRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpecialistRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SpecialistRepository {
    override fun getSpecialist(): Flow<Specialist> = callbackFlow {
        val specId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val specDocument = firestore.collection("specialists").document(specId)

        val listener = specDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val spec = snapshot?.toObject(Specialist::class.java)
            if (spec != null) {
                spec.id = specId
                trySend(spec).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateSpecialis(specialist: Specialist) {
        val specId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        val specDocument = firestore.collection("specialists").document(specId)
        specDocument.update(
            "name", specialist.name,
            "surname", specialist.surname,
            "phoneNumber", specialist.phoneNumber,
            "email", specialist.email,
            "about", specialist.about,
            "address", specialist.address,
            "conditions", specialist.conditions,
            "experience", specialist.experience,
            "specialization", specialist.specialization,
            "location", specialist.location
        ).await()
    }
}