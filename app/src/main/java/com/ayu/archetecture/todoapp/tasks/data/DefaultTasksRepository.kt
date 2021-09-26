package com.ayu.archetecture.todoapp.tasks.data

import androidx.lifecycle.LiveData
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.source.TasksDataSource
import kotlinx.coroutines.*

/**
 * Author: allenwang
 * Date: 2021/9/15 17:20
 * Description: 任务仓库默认实现
 *  耗时任务：切换到携程
 */
class DefaultTasksRepository(
    private val tasksRemoteDataSource: TasksDataSource,
    private val tasksLocalDataSource : TasksDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ): TasksRepository {

    override fun observeTasks(): LiveData<Result<List<Task>>> = tasksLocalDataSource.observeTasks()

    override fun observeTask(taskId: String): LiveData<Result<Task>> = tasksLocalDataSource.observeTask(taskId)

    override suspend fun refreshTasks() = updateTasksFromRemoteDataSource()

    override suspend fun refreshTask(taskId: String) = updateTasksFromRemoteDataSource(taskId)

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        if( forceUpdate ) {
            try {
                updateTasksFromRemoteDataSource()
            }catch (e: Exception) {
                return Result.Error(e)
            }
        }
        return tasksLocalDataSource.getTasks()
    }

    private suspend fun updateTasksFromRemoteDataSource() {
        val remoteTasks = tasksRemoteDataSource.getTasks()

        if( remoteTasks is Result.Success ) {
            tasksLocalDataSource.deleteAllTasks()
            remoteTasks.data.forEach {
                tasksLocalDataSource.saveTask(it)
            }
        } else if( remoteTasks is Result.Error ) {
            throw remoteTasks.exception
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        if( forceUpdate ) {
            updateTasksFromRemoteDataSource(taskId)
        }
        return tasksLocalDataSource.getTask(taskId)
    }

    private suspend fun updateTasksFromRemoteDataSource(taskId: String) {
        val remoteTask = tasksRemoteDataSource.getTask(taskId)
        if( remoteTask is Result.Success ) {
            tasksLocalDataSource.saveTask(remoteTask.data)
        }
    }

    override suspend fun saveTask(task: Task) {
        coroutineScope {
            launch { tasksRemoteDataSource.saveTask(task) }
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }

    override suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksRemoteDataSource.deleteAllTasks() }
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksRemoteDataSource.deleteTask(taskId) }
                launch { tasksLocalDataSource.deleteTask(taskId) }
            }
        }
    }

    override suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { tasksRemoteDataSource.completeTask(task) }
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    override suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Result.Success)?.let {
                completeTask(it.data)
            }
        }
    }

    private suspend fun getTaskWithId(taskId: String): Result<Task> {
        return tasksRemoteDataSource.getTask(taskId)
    }

    override suspend fun activateTask(task: Task) {
        coroutineScope {
            launch { tasksRemoteDataSource.activeTask(task) }
            launch { tasksLocalDataSource.activeTask(task) }
        }
    }

    override suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Result.Success)?.let { it ->
                activateTask(it.data)
            }
        }
    }

    override suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { tasksRemoteDataSource.clearCompletedTasks() }
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
    }

}