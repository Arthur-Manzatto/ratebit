package com.example.ratebit.model

data class Rating(
    val gameId: Int,
    val userEmail: String,
    val score: Double,
    val comment: String
)