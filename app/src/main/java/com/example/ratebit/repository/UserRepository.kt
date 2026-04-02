package com.example.ratebit.repository

import com.example.ratebit.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersCollection.document(user.email).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getUser(email: String, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        usersCollection.document(email).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onSuccess(user)
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersCollection.document(user.email).set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteUser(email: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        usersCollection.document(email).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // FAVORITES
    fun toggleFavorite(userEmail: String, gameId: Int, isFavorite: Boolean, onSuccess: () -> Unit) {
        val favRef = usersCollection.document(userEmail).collection("favorites").document(gameId.toString())
        
        if (isFavorite) {
            favRef.set(mapOf("id" to gameId)).addOnSuccessListener { onSuccess() }
        } else {
            favRef.delete().addOnSuccessListener { onSuccess() }
        }
    }

    fun getFavoriteIds(userEmail: String, onSuccess: (List<Int>) -> Unit) {
        usersCollection.document(userEmail).collection("favorites").get()
            .addOnSuccessListener { snapshots ->
                val ids = snapshots.map { it.id.toInt() }
                onSuccess(ids)
            }
    }

    // NEW: Observe favorites in real time
    fun observeFavoriteIds(userEmail: String, onResult: (List<Int>) -> Unit): ListenerRegistration {
        return usersCollection.document(userEmail).collection("favorites")
            .addSnapshotListener { snapshots, _ ->
                if (snapshots != null) {
                    val ids = snapshots.mapNotNull { it.id.toIntOrNull() }
                    onResult(ids)
                }
            }
    }
}