package com.example.dogownerapp.data.repository

import com.example.dogownerapp.domain.model.Recommendation
import com.example.dogownerapp.domain.repository.RecommendRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RecommendRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : RecommendRepository {
    override fun getRecommends(): Flow<List<Recommendation>> = callbackFlow {
        val recCollection = firestore.collection("recommendations")
        val listener = recCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val recs = snapshot?.documents?.mapNotNull { it.toObject(Recommendation::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(recs).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override fun getRecommendById(recId: String): Flow<Recommendation> = callbackFlow {
        val recDocument = firestore.collection("recommendations").document(recId)
        val listener = recDocument.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val rec = snapshot?.toObject(Recommendation::class.java)
            if (rec != null) {
                trySend(rec).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }
}