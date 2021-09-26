package com.ayu.archetecture.todoapp.tasks.data

import androidx.lifecycle.LiveData
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
/**
 * Author: allenwang
 * Date: 2021/9/13 15:47
 * Description: 任务仓库接口，增删改查、刷新
 */
interface TasksRepository {

    fun observeTasks(): LiveData<Result<List<Task>>>

    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun refreshTasks()

    suspend fun refreshTask(taskId: String)

    suspend fun getTasks(forceUpdate: Boolean = false): Result<List<Task>>

    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Result<Task>

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)

}