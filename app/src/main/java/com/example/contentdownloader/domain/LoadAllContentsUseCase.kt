package com.example.contentdownloader.domain

import com.example.contentdownloader.repository.ContentRepository
import javax.inject.Inject

class LoadAllContentsUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) {
    suspend operator fun invoke() = contentRepository.loadAllContent()
}