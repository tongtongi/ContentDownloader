package com.example.contentdownloader.domain

import com.example.contentdownloader.database.Content
import com.example.servicetest.FakeContentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the implementation of [IsContentExistsUseCase]
 */
@ExperimentalCoroutinesApi
class IsContentExistsUseCaseTest {
    private val contentRepository = FakeContentRepository()

    private val useCase = IsContentExistsUseCase(contentRepository)

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
    fun `return false when repository is empty`() {
        runBlocking {
            val isExist = useCase(content.contentUrl)
            assertFalse(isExist)
        }
    }

    @Test
    fun `return true when content is exist in repository`() {
        runBlocking {
            contentRepository.addContents(content)
            val isExist = useCase(content.contentUrl)
            assertTrue(isExist)
        }
    }

    @Test
    fun `return false when content is not exist in repository`() {
        runBlocking {
            val isExist = useCase(contentUrl = "url2")
            assertFalse(isExist)
        }
    }
}