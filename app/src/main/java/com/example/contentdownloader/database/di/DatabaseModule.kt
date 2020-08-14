package com.example.contentdownloader.database.di

import android.content.Context
import androidx.room.Room
import com.example.contentdownloader.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DataBaseModule {

    @Provides
    fun provideContentDao(database: AppDatabase) = database.contentDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "contents.db"
    ).fallbackToDestructiveMigration().build()
}