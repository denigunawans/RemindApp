package com.dng.remindapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dng.remindapp.database.dao.FinancialRecordDao
import com.dng.remindapp.database.dao.RecordCategoryDao
import com.dng.remindapp.model.FinancialRecord
import com.dng.remindapp.model.RecordCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancialRecordViewModel @Inject constructor(
    private val recordDao: FinancialRecordDao,
    private val categoryDao: RecordCategoryDao
) :
    ViewModel() {
    private val _state: MutableState<List<FinancialRecord>> = mutableStateOf(emptyList())
    val state: State<List<FinancialRecord>> = _state

    private val _categories: MutableState<List<RecordCategory>> = mutableStateOf(emptyList())
    val categories: State<List<RecordCategory>> = _categories

    private val _totalIncome: MutableState<Double> = mutableDoubleStateOf(0.0)
    val totalIncome: State<Double> = _totalIncome

    private val _totalOutcome: MutableState<Double> = mutableDoubleStateOf(0.0)
    val totalOutcome: State<Double> = _totalOutcome

    init {
        getAllRecord()
        getAllCategory()
    }

    private fun getAllRecord() {
        CoroutineScope(Dispatchers.IO).launch {
            _state.value = recordDao.getAllRecord()
            _totalIncome.value = 0.0
            _totalOutcome.value = 0.0
            state.value.forEach { record ->
                if (record.isIncome) {
                    _totalIncome.value += record.nominal
                } else {
                    _totalOutcome.value += record.nominal
                }
            }
        }
    }

    fun insertRecord(record: FinancialRecord) {
        runQuery {
            recordDao.insertRecord(record)
            getAllRecord()
        }
    }

    fun updateRecord(record: FinancialRecord) {
        runQuery {
            recordDao.updateRecord(record)
            getAllRecord()
        }
    }

    fun deleteRecord(record: FinancialRecord) {
        runQuery {
            recordDao.deleteRecord(record)
            getAllRecord()
        }
    }

    // categories query

    private fun getAllCategory() {
        CoroutineScope(Dispatchers.IO).launch {
            _categories.value = categoryDao.getAllCategory().sortedBy { it.category }
            if (categories.value.isEmpty()) {
                insertDefaultCategories()
            }
        }
    }

    private fun insertDefaultCategories() {
        val defaultCategory = listOf(
            "Makanan", "Belanja", "Transportasi", "Sosial", "Elektronik", "Kesehatan", "Lain - lain"
        )

        runQuery {
            defaultCategory.forEach { category ->
                categoryDao.insertCategory(RecordCategory(category = category))
            }
            getAllCategory()
        }

    }

    fun insertCategory(category: RecordCategory) {
        runQuery {
            categoryDao.insertCategory(category)
            getAllCategory()
        }
    }

    fun updateCategory(category: RecordCategory) {
        runQuery {
            categoryDao.updateCategory(category)
            getAllCategory()
        }
    }

    fun deleteCategory(category: RecordCategory) {
        runQuery {
            categoryDao.deleteCategory(category)
            getAllCategory()
        }
    }

    private fun runQuery(query: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            query()
        }
    }
}