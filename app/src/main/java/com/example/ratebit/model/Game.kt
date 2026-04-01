package com.example.ratebit.model

data class Game(
    var idJogo: Int = 0,
    var nomeJogo: String = "",
    var categoriaJogo: String = "",
    var dataJogo: String = "",
    var empresaJogo: String = "",
    var notaMediaJogo: Double = 0.0,
    var descricaoJogo: String = ""
)