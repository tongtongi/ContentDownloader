package com.example.servicetest

import androidx.annotation.VisibleForTesting
import com.example.contentdownloader.database.Content
import com.example.contentdownloader.repository.ContentRepository

class FakeContentRepository : ContentRepository {

    private val fakeDb = hashMapOf<String, Content>()

    override suspend fun loadAllContent() = fakeDb.map { it.value }

    override suspend fun isContentExists(contentUrl: String) = fakeDb.containsKey(contentUrl)

    override suspend fun insert(content: Content): Boolean {
        fakeDb[content.contentUrl] = content
        return true
    }

    override suspend fun delete(content: Content): Boolean {
        fakeDb.remove(content.contentUrl)
        return true
    }

    @VisibleForTesting
    fun addContents(vararg contents: Content) {
        for (content in contents) {
            fakeDb[content.contentUrl] = content
        }
    }

    @VisibleForTesting
    fun deleteAllContents(vararg contents: Content) {
        fakeDb.clear()
    }
}