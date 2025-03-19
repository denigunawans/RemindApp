package com.dng.remindapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.model.RecordCategory

@Dao
interface FinancialRecordDao {

    @Query("SELECT * FROM financial_records")
    fun getAllRecord(): List<FinancialRecord>

    @Query("SELECT * FROM financial_records WHERE id = :id")
    fun getRecordById(id: Int): FinancialRecord

    @Insert
    suspend fun insertRecord(record: FinancialRecord)

    @Update
    suspend fun updateRecord(record: FinancialRecord)

    @Delete
    suspend fun deleteRecord(record: FinancialRecord)
}

@Dao
interface RecordCategoryDao {
    @Query("SELECT * FROM record_categories")
    fun getAllCategory(): List<RecordCategory>

    @Insert
    fun insertCategory(category: RecordCategory)

    @Update
    fun updateCategory(category: RecordCategory)

    @Delete
    fun deleteCategory(category: RecordCategory)
}


