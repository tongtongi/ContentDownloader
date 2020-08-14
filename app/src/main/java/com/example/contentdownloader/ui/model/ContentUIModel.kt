package com.example.contentdownloader.ui.model

data class ContentUIModel(
    val name: String,
    val url: String,
    val path: String,
    val progress: Int,
    val readLength: Long = 0,
    val totalLength: Long = 0,
    val isCompleted: Boolean = false,
    val isPaused: Boolean = true
)