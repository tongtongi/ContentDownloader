package com.example.contentdownloader.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Content::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contentDao(): ContentDao
}