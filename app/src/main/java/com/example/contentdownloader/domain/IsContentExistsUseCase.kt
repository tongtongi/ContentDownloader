package com.example.contentdownloader.domain

import com.example.contentdownloader.repository.ContentRepository
import javax.inject.Inject

class IsContentExistsUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) {
    suspend operator fun invoke(contentUrl: String) = contentRepository.isContentExists(contentUrl)
}