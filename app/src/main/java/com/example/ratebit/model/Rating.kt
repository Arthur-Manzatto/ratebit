package com.example.ratebit.model

data class Rating(
    val fkGameId: Int,
    val fkEmailUser: String,
    val rating: Double,
    val comment: String
)
