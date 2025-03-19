package com.dng.remindapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dng.remindapp.model.Todo

@Dao
interface TodolistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(task: Todo)

    @Update
    suspend fun update(task: Todo)

    @Query("UPDATE todolist SET is_done = CASE WHEN is_done = 1 THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun setDoneTask(id: Int)

    @Delete
    suspend fun delete(task: Todo)

    @Query("SELECT * FROM todolist WHERE deadline = :date")
    fun getTaskByDeadline(date: String): Todo

    @Query("SELECT * FROM todolist WHERE id = :id")
    fun getTaskById(id: Int): Todo

    @Query("SELECT * FROM todolist")
    fun getAllTodolist(): List<Todo>

    @Query("SELECT * FROM todolist WHERE deadline = :deadline")
    fun geTodayTodolist(deadline: String): List<Todo>
}