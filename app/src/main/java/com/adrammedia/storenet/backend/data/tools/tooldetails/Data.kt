package com.adrammedia.storenet.backend.data.tools.tooldetails

data class Data(
    val toolCategory: String,
    val toolDescription: String,
    val toolId: Int,
    val toolImages: List<ToolImage>,
    val toolName: String,
    val toolPrice: Int,
    val toolState: String,
    val toolUser: String
)