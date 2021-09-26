package com.ayu.archetecture.todoapp.tasks.data.source

import androidx.lifecycle.LiveData
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.entity.Task

/**
 * Author: allenwang
 * Date: 2021/9/15 17:31
 * Description: 任务数据源接口
 */
interface TasksDataSource {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun refreshTasks()

    suspend fun refresh(taskId: String)

    suspend fun getTasks(): Result<List<Task>>

    suspend fun getTask(taskId: String): Result<Task>

    suspend fun saveTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)

    suspend fun completeTask(task: Task)

    suspend fun activeTask(task: Task)

    suspend fun clearCompletedTasks()

}