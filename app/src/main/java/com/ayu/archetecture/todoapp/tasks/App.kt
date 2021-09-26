package com.ayu.archetecture.todoapp.tasks

import android.app.Application
import com.ayu.archetecture.todoapp.tasks.data.TasksRepository

/**
 * Author: allenwang
 * Date: 2021/9/24 11:51
 * Description:
 */
class App: Application() {

    // 后续可改为dagger注入
    // 这里可保证只有一个实例
    val tasksRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
    }
}