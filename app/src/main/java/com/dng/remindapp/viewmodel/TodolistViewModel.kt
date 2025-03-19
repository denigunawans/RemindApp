package com.dng.remindapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dng.remindapp.database.dao.TodolistDao
import com.dng.remindapp.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodolistViewModel @Inject constructor(private val dao: TodolistDao) : ViewModel() {

    private val _state: MutableState<List<Todo>> = mutableStateOf(emptyList())
    val state: State<List<Todo>> = _state

    private val resultQuery: MutableState<String> = mutableStateOf("")

    init {
        getAllTodolist()
    }

    private fun getAllTodolist() {
        CoroutineScope(Dispatchers.IO).launch {
            runQuery {
                _state.value = dao.getAllTodolist()
            }
        }
    }

    fun insertTodo(todo: Todo) {
        runQuery {
            dao.insert(todo)
            getAllTodolist()
        }
    }

    fun updateTodo(todo: Todo) {
        runQuery {
            dao.update(todo)
            getAllTodolist()
        }
    }

    fun setTaskDone(todo: Todo) {
        runQuery {
            dao.setDoneTask(todo.id)
            getAllTodolist()
        }
    }

    fun deleteTodo(todo: Todo) {
        runQuery {
            dao.delete(todo)
            getAllTodolist()
        }
    }

    private fun runQuery(order: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                order()
                resultQuery.value = "Success"
            } catch (e: Exception) {
                resultQuery.value = "Failed: $e"
            }
        }
    }
}