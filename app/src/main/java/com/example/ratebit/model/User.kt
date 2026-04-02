package com.example.ratebit.model

data class User(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var type: String = "",
    var urlPfp: String = ""
)