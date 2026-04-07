package com.example.ratebit.repository

import com.example.ratebit.model.Game
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class GameRepository {
    private val db = FirebaseFirestore.getInstance()
    private val gamesCollection = db.collection("games")

    fun addGame(game: Game, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        gamesCollection.add(game)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun observeGames(onResult: (List<Game>) -> Unit, onError: (Exception) -> Unit): ListenerRegistration {
        return gamesCollection.addSnapshotListener { snapshots, e ->
            if (e != null) {
                onError(e)
                return@addSnapshotListener
            }
            if (snapshots != null) {
                val games = snapshots.toObjects(Game::class.java)
                onResult(games)
            }
        }
    }

    fun getGameById(id: Int, onResult: (Game?) -> Unit, onError: (Exception) -> Unit) {
        gamesCollection.whereEqualTo("id", id).limit(1).get()
            .addOnSuccessListener { snapshots ->
                if (!snapshots.isEmpty) {
                    val game = snapshots.documents[0].toObject(Game::class.java)
                    onResult(game)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { onError(it) }
    }
}