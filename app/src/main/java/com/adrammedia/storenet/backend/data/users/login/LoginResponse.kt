package com.adrammedia.storenet.backend.data.users.login

data class LoginResponse(
    val data: User,
    val message: String,
    val status: Int
)