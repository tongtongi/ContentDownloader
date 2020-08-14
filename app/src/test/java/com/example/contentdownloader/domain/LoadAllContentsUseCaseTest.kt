package com.example.contentdownloader.domain

import com.example.contentdownloader.database.Content
import com.example.contentdownloader.domain.LoadAllContentsUseCase
import com.example.servicetest.FakeContentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


/**
 * Unit tests for the implementation of [LoadAllContentsUseCase]
 */
@ExperimentalCoroutinesApi
class LoadAllContentsUseCaseTest {
    private val contentRepository = FakeContentRepository()

    private val useCase = LoadAllContentsUseCase(contentRepository)

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
    fun `load contents when repository is empty`() = runBlocking {
        // GIVEN an empty repository

        // Load contents
        val result = useCase()

        // Verify  the result is empty
        assertTrue(result.isEmpty())
    }

    @Test
    fun `load contents when repository is not empty`() = runBlocking {
        // Given a repository with 2 contents
        contentRepository.addContents(content, content.copy(contentUrl = "url2"))

        // Load task
        val result = useCase()

        // Verify  the result
        assertEquals(result.size, 2)
    }
}