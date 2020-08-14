package com.example.contentdownloader.repository

import android.util.Log
import com.example.contentdownloader.database.AppDatabase
import com.example.contentdownloader.database.Content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ContentRepository {
    suspend fun loadAllContent(): List<Content>
    suspend fun isContentExists(contentUrl: String): Boolean
    suspend fun insert(content: Content): Boolean
    suspend fun delete(content: Content): Boolean
}

class ContentRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : ContentRepository {

    override suspend fun loadAllContent(): List<Content> {
        return withContext(Dispatchers.IO) {
            database.contentDao().loadAllContents()
        }
    }

    override suspend fun isContentExists(contentUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            database.contentDao().isContentExist(contentUrl)
        }
    }

    override suspend fun insert(content: Content): Boolean {
        try {
            withContext(Dispatchers.IO) {
                database.contentDao().insert(content)
            }
            return true
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
        return false
    }

    override suspend fun delete(content: Content): Boolean {
        try {
            withContext(Dispatchers.IO) {
                database.contentDao().delete(content)
            }
            return true
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
        return false
    }
}