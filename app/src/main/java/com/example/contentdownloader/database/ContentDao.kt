package com.example.contentdownloader.database

import androidx.room.*

@Dao
interface ContentDao {

    @Query("SELECT * FROM contents ORDER BY fileName ASC")
    fun loadAllContents(): List<Content>

    @Query("SELECT EXISTS(SELECT * FROM contents WHERE contentUrl = :contentUrl)")
    fun isContentExist(contentUrl: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(content: Content)

    @Delete
    fun delete(content: Content)
}