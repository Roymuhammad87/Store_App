package com.adrammedia.storenet.backend.data.users.profile

data class StoreProfileResponse(
    val profile: Profile,
    val message: String,
    val status: Int
)