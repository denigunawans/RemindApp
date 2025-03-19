package com.dng.remindapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dng.remindapp.database.dao.NoteDao
import com.dng.remindapp.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val dao: NoteDao) : ViewModel() {
    private val _state: MutableState<List<Note>> = mutableStateOf(emptyList())
    val state: State<List<Note>> = _state

    init {
        getAllNote()
    }

    private fun getAllNote() {
        CoroutineScope(Dispatchers.IO).launch {
            _state.value = dao.getAllNote().sortedBy { it.lastModified.toLong() }.reversed()
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
            getAllNote()
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            dao.updateNote(note)
            getAllNote()
            println("UPDATE view model called : $note")
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
            getAllNote()
        }
    }
}