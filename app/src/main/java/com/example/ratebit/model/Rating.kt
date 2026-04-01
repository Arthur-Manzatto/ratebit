package com.example.ratebit.model

data class Rating(
    val fkIdJogo: Int,
    val fkEmailUser: String,
    val nota: Double,
    val comentario: String
)
