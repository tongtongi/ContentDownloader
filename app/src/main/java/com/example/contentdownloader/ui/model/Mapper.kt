package com.example.contentdownloader.ui.model

import com.example.contentdownloader.database.Content

private const val COMPLETE_PERCENTAGE = 100

fun Content.toUIModel() = ContentUIModel(
    name = this.fileName,
    url = this.contentUrl,
    path = this.filePath,
    progress = this.progress,
    isCompleted = this.progress == COMPLETE_PERCENTAGE,
    isPaused = this.isPaused,
    totalLength = this.totalLength,
    readLength = this.readLength
)

fun ContentUIModel.toEntityModel() = Content(
    contentUrl = this.url,
    fileName = this.name,
    filePath = this.path,
    totalLength = this.totalLength,
    readLength = this.readLength,
    progress = this.progress,
    isPaused = this.isPaused
)