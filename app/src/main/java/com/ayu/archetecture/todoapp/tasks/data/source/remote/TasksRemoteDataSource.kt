package com.ayu.archetecture.todoapp.tasks.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.data.source.TasksDataSource
import kotlinx.coroutines.delay

/**
 * Author: allenwang
 * Date: 2021/9/24 14:05
 * Description: 远程数据源，本地mock，模拟延时
 */
object TasksRemoteDataSource: TasksDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L
    private var TASKS_SERVICE_DATA = LinkedHashMap<String, Task>(2)
    init {
        addTask("跑步", "户外跑步3公里，健走2公里")
        addTask("读书", "阅读《富兰克林传记》2个章节")
    }

    private fun addTask(title: String, description: String) {
        val newTask = Task(title = title, description = description)
        TASKS_SERVICE_DATA[newTask.id] = newTask
    }

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    override fun observeTasks(): LiveData<Result<List<Task>>> = observableTasks

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return observableTasks.map { tasks ->
            when(tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId } ?: return@map Result.Error(
                        Exception("没有找到")
                    )
                    Result.Success(task)
                }
            }
        }
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()!!
    }

    override suspend fun refresh(taskId: String) {
        refreshTasks()
    }

    override suspend fun getTasks(): Result<List<Task>> {
        val tasks = TASKS_SERVICE_DATA.values.toList()
        mockDelay()
        return Result.Success(tasks)
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        mockDelay()
        TASKS_SERVICE_DATA[taskId]?.let { return Result.Success(it) }
        return Result.Error(Exception("没找到任务"))
    }

    override suspend fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }

    override suspend fun completeTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = Task(task.id, task.title, task.description, true)
    }

    override suspend fun activeTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = Task(task.id, task.title, task.description, false)
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues { !it.isCompleted } as LinkedHashMap<String, Task>
    }

    private suspend fun mockDelay() {
        delay(SERVICE_LATENCY_IN_MILLIS)
    }
}