package com.adrammedia.storenet.backend.data.tools.alltools

data class Tool(
    val toolCategory: String,
    val toolDescription: String,
    val toolId: Int,
    val toolImages: List<ToolImage>,
    val toolName: String,
    val toolPrice: Int,
    val toolState: Boolean,
    val toolUser: String
)