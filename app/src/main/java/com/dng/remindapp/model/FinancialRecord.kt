package com.dng.remindapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_records")
data class FinancialRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val desc: String,
    val category: String,
    val nominal: Int,
    @ColumnInfo(name = "is_income")
    val isIncome: Boolean,
    val date: String
)

@Entity(tableName = "record_categories")
data class RecordCategory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String
)

data class WalletBalance(
    val income: Double,
    val outcome: Long
)
