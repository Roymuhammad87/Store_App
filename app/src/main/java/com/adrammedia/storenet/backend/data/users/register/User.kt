package com.adrammedia.storenet.backend.data.users.register

data class User(
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String,
)