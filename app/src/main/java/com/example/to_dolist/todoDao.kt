package com.example.to_dolist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface todoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTodo(todo: MyTodo)

    @Query("SELECT * FROM MyTodo")
    fun selectAllTodo(): LiveData<List<MyTodo>>

    @Delete
    fun deleteTodo(todo: MyTodo)

    @Update
    fun updateTodo(todo: MyTodo)

}