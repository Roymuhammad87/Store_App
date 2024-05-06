package com.adrammedia.storenet.backend.data.users.register

data class RegisterResponse(
    val data: User,
    val message: String,
    val status: Int
)