package com.example.contentdownloader.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contents")
data class Content(
    @PrimaryKey
    val contentUrl: String,
    val fileName: String,
    val filePath: String,
    val totalLength: Long,
    val readLength: Long,
    val progress: Int,
    val isPaused: Boolean
)