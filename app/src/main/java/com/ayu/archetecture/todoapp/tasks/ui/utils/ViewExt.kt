package com.ayu.archetecture.todoapp.tasks.ui.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar

/**
 * Author: allenwang
 * Date: 2021/9/25 16:02
 * Description: 视图相关扩展方法
 */

fun View.setupSnackBar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner) { event ->
        event.getContentIfNotHandled()?.also { showSnackbar(context.getString(it), timeLength)}
    }
}

fun View.showSnackbar(text:String, timeLength: Int) {
    Snackbar.make(this, text, timeLength).run {
        show()
    }
}