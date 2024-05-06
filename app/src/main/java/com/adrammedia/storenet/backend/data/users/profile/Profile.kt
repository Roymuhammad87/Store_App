package com.adrammedia.storenet.backend.data.users.profile

data class Profile(
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String
)