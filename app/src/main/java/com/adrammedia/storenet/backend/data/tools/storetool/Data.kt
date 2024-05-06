package com.adrammedia.storenet.backend.data.tools.storetool

data class Data(
    val toolCategory: String,
    val toolDescription: String,
    val toolId: Int,
    val toolImages: List<ToolImage>,
    val toolName: String,
    val toolPrice: String,
    val toolState: Boolean,
    val toolUser: String
)