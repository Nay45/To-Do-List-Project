package com.example.to_dolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class MyTodo (
    @PrimaryKey(autoGenerate = true)
    val todoId: Int? = null,
    var title: String,
    var desc: String,
    var date: String,
    var category: String
) : Serializable