package com.dng.remindapp.di

import android.content.Context
import androidx.room.Room
import com.dng.remindapp.database.AppDatabase
import com.dng.remindapp.database.dao.FinancialRecordDao
import com.dng.remindapp.database.dao.NoteDao
import com.dng.remindapp.database.dao.RecordCategoryDao
import com.dng.remindapp.database.dao.TodolistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "main.db").build()
    }

    @Provides
    @Singleton
    fun provideTodolistDao(appDatabase: AppDatabase): TodolistDao {
        return appDatabase.todolistDao()
    }

    @Provides
    @Singleton
    fun provideNotesDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.notesDao()
    }

    @Provides
    @Singleton
    fun provideFinancialRecordDao(appDatabase: AppDatabase): FinancialRecordDao {
        return appDatabase.financialRecordDao()
    }

    @Provides
    @Singleton
    fun provideRecordCategoryDao(appDatabase: AppDatabase): RecordCategoryDao {
        return appDatabase.recordCategoryDao()
    }
}