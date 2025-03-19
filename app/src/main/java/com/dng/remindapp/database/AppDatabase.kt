package com.dng.remindapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dng.remindapp.database.dao.FinancialRecordDao
import com.dng.remindapp.database.dao.NoteDao
import com.dng.remindapp.database.dao.RecordCategoryDao
import com.dng.remindapp.database.dao.TodolistDao
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.model.Note
import com.dng.remindapp.model.RecordCategory
import com.dng.remindapp.model.Todo

@Database(entities = [Todo::class, Note::class, FinancialRecord::class, RecordCategory::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun todolistDao(): TodolistDao
    abstract fun notesDao(): NoteDao
    abstract fun financialRecordDao(): FinancialRecordDao
    abstract fun recordCategoryDao(): RecordCategoryDao
}