package com.example.to_dolist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyTodo::class], version = 1)
abstract class todoDatabase: RoomDatabase() {
    abstract fun todoDao(): todoDao
    companion object{
        var INSTANCE: todoDatabase? = null
        fun getTodoDatabase(context: Context): todoDatabase? {
            if (INSTANCE == null){
                synchronized(todoDatabase::class){
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext,
                            todoDatabase::class.java, "Todo-List").build()
                }
            }
            return INSTANCE
        }
    }
}