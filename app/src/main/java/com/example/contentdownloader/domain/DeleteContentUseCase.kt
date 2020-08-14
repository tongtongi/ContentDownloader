package com.example.contentdownloader.domain

import com.example.contentdownloader.database.Content
import com.example.contentdownloader.repository.ContentRepository
import javax.inject.Inject

class DeleteContentUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) {
    suspend operator fun invoke(content: Content) = contentRepository.delete(content)
}