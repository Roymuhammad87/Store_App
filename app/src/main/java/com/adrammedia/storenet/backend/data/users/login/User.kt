package com.adrammedia.storenet.backend.data.users.login

data class User(
    val id:Int,
    val email: String,
    val password: String,
    val token: String? = null
)