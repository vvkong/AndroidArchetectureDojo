package com.ayu.archetecture.todoapp.tasks.data.entity


/**
 * Author: allenwang
 * Date: 2021/9/15 15:05
 * Description: 加载状态封装
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T): Result<T>()

    data class Error(val exception: Exception): Result<Nothing>()

    object  Loading: Result<Nothing>()

    override fun toString(): String {
        return when(this) {
            is Success -> "success: $data"
            is Error -> "Error: $exception"
            Loading -> "Loading"
        }
    }
}