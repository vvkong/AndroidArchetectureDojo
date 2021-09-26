package com.ayu.archetecture.todoapp.tasks

import android.content.Context
import androidx.room.Room
import com.ayu.archetecture.todoapp.tasks.data.DefaultTasksRepository
import com.ayu.archetecture.todoapp.tasks.data.TasksRepository
import com.ayu.archetecture.todoapp.tasks.data.source.local.AppDatabase
import com.ayu.archetecture.todoapp.tasks.data.source.local.TaskLocalDataSource
import com.ayu.archetecture.todoapp.tasks.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking

/**
 * Author: allenwang
 * Date: 2021/9/24 11:53
 * Description: 实体创造提供类，单例实现
 */
object ServiceLocator {
    private const val DB_NAME = "Tasks.db"
    private val lock = Any()
    private var database: AppDatabase? = null
    @Volatile
    private var tasksRepository: TasksRepository? = null

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TaskLocalDataSource {
        val db = database ?: createDataBase(context)
        return TaskLocalDataSource(db.taskDao())
    }

    private fun createDataBase(context: Context): AppDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        ).build()
        database = result
        return result
    }

    fun resetRepository() {
        synchronized(lock) {
            runBlocking { TasksRemoteDataSource.deleteAllTasks() }
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }

}