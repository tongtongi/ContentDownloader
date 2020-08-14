package com.example.contentdownloader.domain

import com.example.contentdownloader.database.Content
import com.example.contentdownloader.repository.ContentRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Unit tests for the implementation of [AddContentUseCase]
 */
@ExperimentalCoroutinesApi
class AddContentUseCaseTest {
    private val contentRepository: ContentRepository = mock()

    private val useCase = AddContentUseCase(contentRepository)

    private val content = Content(
        contentUrl = "url",
        fileName = "fileName",
        filePath = "filePath",
        totalLength = 1230L,
        readLength = 123L,
        progress = 10,
        isPaused = false
    )

    @Test
    fun `insert content into repository when usecase is invoked`() {
        runBlocking {
            useCase(content)
        }

        runBlocking {
            verify(contentRepository).insert(content)
        }
    }
}