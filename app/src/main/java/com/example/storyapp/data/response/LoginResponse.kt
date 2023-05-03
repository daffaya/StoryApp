package com.example.storyapp.data.response

data class LoginResponse(
    val error: Boolean,
//    TODO: LoginResult dibikin nullable jadi LoginResult?
//    Soalnya, kalo error response nya itu gak ada loginResult
    val loginResult: LoginResult,
    val message: String
)

data class LoginResult(
    val name: String,
    val token: String,
    val userId: String
)