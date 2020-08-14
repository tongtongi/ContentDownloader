package com.example.contentdownloader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.contentdownloader.database.Content
import com.example.contentdownloader.domain.AddContentUseCase
import com.example.contentdownloader.domain.DeleteContentUseCase
import com.example.contentdownloader.domain.IsContentExistsUseCase
import com.example.contentdownloader.domain.LoadAllContentsUseCase
import com.example.contentdownloader.ui.download.DownloadViewModel
import com.example.contentdownloader.ui.model.ContentUIModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit tests for the implementation of [DownloadViewModel]
 */
@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DownloadViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val loadAllContentsUseCase: LoadAllContentsUseCase = mock()
    private val addContentUseCase: AddContentUseCase = mock()
    private val deleteContentUseCase: DeleteContentUseCase = mock()
    private val isContentExistsUseCase: IsContentExistsUseCase = mock()

    private val entity = Content(
        contentUrl = "url",
        fileName = "fileName",
        filePath = "filePath",
        totalLength = 1230L,
        readLength = 123L,
        progress = 10,
        isPaused = false
    )

    private val uiModel = ContentUIModel(
        url = "url",
        name = "fileName",
        path = "filePath",
        totalLength = 1230L,
        readLength = 123L,
        progress = 10,
        isPaused = false,
        isCompleted = false
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `should return correct list when query is successful`() {
        // GIVEN
        val expectedResult = listOf(uiModel, uiModel.copy(url = "url2"))

        runBlocking {
            whenever(loadAllContentsUseCase()).thenReturn(mockResponse())
        }

        // WHEN
        val viewModel = DownloadViewModel(
            addContentUseCase,
            loadAllContentsUseCase,
            deleteContentUseCase,
            isContentExistsUseCase
        )

        // THEN
        val actual = viewModel.contentList.value

        Assert.assertEquals(expectedResult, actual)
    }

    @Test
    fun `should return empty list when db is empty`() {
        // GIVEN
        val expectedResult = emptyList<ContentUIModel>()

        runBlocking {
            whenever(loadAllContentsUseCase()).thenReturn(emptyList())
        }

        // WHEN
        val viewModel = DownloadViewModel(
            addContentUseCase,
            loadAllContentsUseCase,
            deleteContentUseCase,
            isContentExistsUseCase
        )

        // THEN
        val actual = viewModel.contentList.value

        Assert.assertEquals(expectedResult, actual)
    }

    private fun mockResponse() =
        listOf(entity, entity.copy(contentUrl = "url2"))
}