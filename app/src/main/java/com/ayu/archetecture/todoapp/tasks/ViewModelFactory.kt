package com.ayu.archetecture.todoapp.tasks

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ayu.archetecture.todoapp.tasks.data.TasksRepository
import com.ayu.archetecture.todoapp.tasks.list.TasksViewModel
import java.lang.IllegalArgumentException

/**
 * Author: allenwang
 * Date: 2021/9/23 18:05
 * Description: viewmodel工厂类，生产viewmodel，协助传递参数：TasksRepository
 */
class ViewModelFactory constructor(
    private val tasksRepository: TasksRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
    ): AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = with(modelClass) {
       when {
            isAssignableFrom(TasksViewModel::class.java) -> {
                TasksViewModel(tasksRepository, handle)
            }
            else -> throw IllegalArgumentException("未知ViewModel类: ${modelClass.name}")
        }
    } as T

}