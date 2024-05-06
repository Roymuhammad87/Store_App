package com.adrammedia.storenet.backend.data.tools.alltools

data class AllToolsResponse(
    val data: List<Tool>,
    val message: String,
    val status: Int
)