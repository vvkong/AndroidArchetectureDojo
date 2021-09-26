package com.ayu.archetecture.todoapp.tasks.data.source.local

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.data.source.TasksDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.Exception

/**
 * Author: allenwang
 * Date: 2021/9/18 11:12
 * Description: 任务本地数据源实现
 *  注意：操作切换到io携程
 */
class TaskLocalDataSource internal constructor(
    private val tasksDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): TasksDataSource {

    override fun observeTasks(): LiveData<Result<List<Task>>> = tasksDao.observeTasks().map { Result.Success(it) }

    override fun observeTask(taskId: String): LiveData<Result<Task>> = tasksDao.observeTaskById(taskId).map { Result.Success(it) }

    override suspend fun refreshTasks() {
        // NO OP
    }

    override suspend fun refresh(taskId: String) {
        // NO OP
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try{
            Result.Success(tasksDao.getTasks())
        }catch (e : Exception ){
            Result.Error(e)
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher) {
        return@withContext try{
            val task = tasksDao.getTaskById(taskId)
            if( task != null ) {
                Result.Success(task)
            } else {
                Result.Error(Resources.NotFoundException())
            }
        } catch (e : Exception ) {
            Result.Error(Resources.NotFoundException())
        }
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insertTask(task)
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        tasksDao.deleteTasks()
    }

    override suspend fun deleteTask(taskId: String) = withContext(ioDispatcher) {
        tasksDao.deleteTaskById(taskId)
        Unit
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun activeTask(task: Task) = withContext( context = ioDispatcher) {
        tasksDao.updateCompleted(task.id, false)
    }

    override suspend fun clearCompletedTasks() = withContext(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }
}