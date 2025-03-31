package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.model.Specialist
import com.example.dogownerapp.domain.repository.RecommendRepository
import com.example.dogownerapp.domain.repository.SearchSpecRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SearchSpecRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SearchSpecRepository {
    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

    override fun getSpecs(): Flow<List<Specialist>> = callbackFlow {
        val specCollection = firestore.collection("specialists")

        val listener = specCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val specs = snapshot?.documents?.mapNotNull { it.toObject(Specialist::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(specs).isSuccess
        }

        awaitClose { listener.remove() }
    }


    override fun getSpecById(specId: String): Flow<Specialist> = callbackFlow {
        val specDocument = firestore.collection("specialists").document(specId)

        val listener = specDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val spec = snapshot?.toObject(Specialist::class.java)
            if (spec != null) {
                trySend(spec).isSuccess
            }
        }

        awaitClose { listener.remove() }
    }


}
