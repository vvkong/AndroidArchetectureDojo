package com.ayu.archetecture.todoapp.tasks.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayu.archetecture.todoapp.tasks.data.entity.Task

/**
 * Author: allenwang
 * Date: 2021/9/14 16:50
 * Description:
 */
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao
}