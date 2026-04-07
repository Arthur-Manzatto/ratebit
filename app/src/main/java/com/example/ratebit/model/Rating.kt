package com.example.ratebit.model

data class Rating(
    var gameId: Int = 0,
    var userEmail: String = "",
    var score: Double = 0.0,
    var comment: String = ""
)