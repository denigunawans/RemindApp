package com.dng.remindapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dng.remindapp.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAllNote(): List<Note>

    @Insert
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}