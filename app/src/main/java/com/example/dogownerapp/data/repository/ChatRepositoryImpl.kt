package com.example.dogownerapp.data.repository

import android.util.Log
import com.example.dogownerapp.domain.model.Chat
import com.example.dogownerapp.domain.model.Message
import com.example.dogownerapp.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ChatRepository {
    private val userId: String = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    override fun getChatsOwner(): Flow<List<Chat>> = callbackFlow {
        val chatCollection = firestore.collection("users").document(userId)
            .collection("chats")
        val listener = chatCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val chats = snapshot?.documents?.mapNotNull { it.toObject(Chat::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(chats).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override fun getChatsSpec(): Flow<List<Chat>> = callbackFlow {
        val chatCollection = firestore.collection("specialists").document(userId)
            .collection("chats")
        val listener = chatCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val chats = snapshot?.documents?.mapNotNull { it.toObject(Chat::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(chats).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override fun getFromOwner(specId: String): Flow<List<Message>> = callbackFlow {
        val mesCollection = firestore.collection("users").document(userId)
            .collection("chats").document(specId).collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val listener = mesCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(messages).isSuccess
        }

        awaitClose { listener.remove() }
    }

    override fun getFromSpec(ownerId: String): Flow<List<Message>> = callbackFlow {
        val mesCollection = firestore.collection("specialists").document(userId)
            .collection("chats").document(ownerId).collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val listener = mesCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val messages = snapshot?.documents?.mapNotNull { it.toObject(Message::class.java)
                ?.copy(id = it.id) } ?: emptyList()
            trySend(messages).isSuccess
        }

        awaitClose { listener.remove() }
    }


    override suspend fun sendFromOwner(specId: String, message: Message) {
        var nameOwner = ""
        getUserName(userId).take(1).collect { name -> nameOwner = name }
        var nameSpec = ""
        getSpecName(specId).take(1).collect { name -> nameSpec = name }
        val chatSender = firestore.collection("users").document(userId)
            .collection("chats").document(specId)
        chatSender.collection("messages").add(message).await()
        val chatReciever = firestore.collection("specialists").document(specId)
            .collection("chats").document(userId)
        val recieved = Message(message.id, message.text, false)
        chatReciever.collection("messages").add(recieved)
        val nameOwnerInfo = mapOf("name" to nameOwner)
        val nameSpecInfo = mapOf("name" to nameSpec)
        chatSender.set(nameSpecInfo)
        chatReciever.set(nameOwnerInfo)
    }

    override suspend fun sendFromSpec(ownerId: String, message: Message) {
        var nameOwner = ""
        getUserName(ownerId).take(1).collect { name -> nameOwner = name }
        var nameSpec = ""
        getSpecName(userId).take(1).collect { name -> nameSpec = name }
        Log.i("name", "$nameOwner $nameSpec")
        val chatSender = firestore.collection("specialists").document(userId)
            .collection("chats").document(ownerId)
        chatSender.collection("messages").add(message).await()

        val chatReciever = firestore.collection("users").document(ownerId)
            .collection("chats").document(userId)
        val recieved = Message(message.id, message.text, false)
        chatReciever.collection("messages").add(recieved)
        val nameOwnerInfo = mapOf("name" to nameOwner)
        val nameSpecInfo = mapOf("name" to nameSpec)
        chatSender.set(nameOwnerInfo)
        chatReciever.set(nameSpecInfo)
    }

     private fun getUserName(userId: String) : Flow<String> = callbackFlow {
        val user = firestore.collection("users").document(userId)
        val listener = user.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val name = snapshot?.data?.get("name").toString()
            val surname = snapshot?.data?.get("surname").toString()
            trySend("$name $surname").isSuccess
        }

        awaitClose { listener.remove() }
    }
    private fun getSpecName(userId: String) : Flow<String> = callbackFlow {
        val user = firestore.collection("specialists").document(userId)
        val listener = user.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val name = snapshot?.data?.get("name").toString()
            val surname = snapshot?.data?.get("surname").toString()

            trySend("$name $surname").isSuccess
        }
        awaitClose { listener.remove() }
    }
}
