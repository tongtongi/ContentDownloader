package com.example.contentdownloader

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.contentdownloader.database.AppDatabase
import com.example.contentdownloader.database.Content
import com.example.contentdownloader.database.ContentDao
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Unit tests for the implementation of [ContentDao]
 */
@RunWith(AndroidJUnit4::class)
class ContentDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var contentDao: ContentDao
    private lateinit var database: AppDatabase

    private val content = Content(
        contentUrl = "url",
        fileName = "fileName",
        filePath = "filePath",
        totalLength = 1230L,
        readLength = 123L,
        progress = 10,
        isPaused = false
    )

    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries() // For test
            .build()

        contentDao = database.contentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun checkContentIsExistAfterInsertion() {
        contentDao.insert(content)

        val isContentExist = contentDao.isContentExist(content.contentUrl)

        assertTrue(isContentExist)
    }

    @Test
    @Throws(Exception::class)
    fun checkContentIsExistAfterDeletion() {
        contentDao.delete(content)

        val isContentExist = contentDao.isContentExist(content.contentUrl)
        assertFalse(isContentExist)
    }
}