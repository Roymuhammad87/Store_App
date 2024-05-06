package com.adrammedia.storenet.backend.data.categories

data class CategoriesResponse(
    val data: List<Category>,
    val message: String,
    val status: Int
)