package com.ayu.archetecture.todoapp.tasks.utils

import androidx.lifecycle.Observer


/**
 * Author: allenwang
 * Date: 2021/9/25 15:38
 * Description: 用于LiveData传递内容的事件包裹
 */
open class Event<out T>(private val content: T) {

    // 外部只读
    var hasBeenHandled = false
        private set

    // 处理事件内容，同时变更处理状态，已处理返回null
    fun getContentIfNotHandled(): T? {
        return if( hasBeenHandled ) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    // 攫取内容
    fun peekContent(): T {
        return content
    }
}

// 事件观察者，简化事件监听处理: 未处理事件才回调处理
class EventObserver<T>(private val onEventUnhandledContent: (T)->Unit): Observer<Event<T>> {
    override fun onChanged(t: Event<T>?) {
        t?.getContentIfNotHandled()?.let { onEventUnhandledContent(it) }
    }
}