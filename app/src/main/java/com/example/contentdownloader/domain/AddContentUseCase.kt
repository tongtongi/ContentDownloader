package com.example.contentdownloader.domain

import com.example.contentdownloader.database.Content
import com.example.contentdownloader.repository.ContentRepository
import javax.inject.Inject

class AddContentUseCase @Inject constructor(
    private val contentRepository: ContentRepository
) {
    suspend operator fun invoke(content: Content) = contentRepository.insert(content)
}