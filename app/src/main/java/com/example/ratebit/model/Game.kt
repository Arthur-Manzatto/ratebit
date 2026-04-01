package com.example.ratebit.model

data class Game(
    var id: Int = 0,
    var name: String = "",
    var category: String = "",
    var releaseDate: String = "",
    var developer: String = "",
    var averageRating: Double = 0.0,
    var description: String = "",
    var coverUrl: String = ""
)