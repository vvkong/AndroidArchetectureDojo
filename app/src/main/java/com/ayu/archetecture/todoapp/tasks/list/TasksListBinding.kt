package com.ayu.archetecture.todoapp.tasks.list

import android.graphics.Paint
import android.service.autofill.TextValueSanitizer
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayu.archetecture.todoapp.tasks.data.entity.Task

/**
 * Author: allenwang
 * Date: 2021/9/24 15:46
 * Description: 任务列表的视图适配绑定
 * 注解、参数（控件、数据）
 */

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Task>?) {
    items?.let {
        ((listView).adapter as TasksListAdapter).submitList(it)
    }
}

@BindingAdapter("app:completedTask")
fun setStyle(textView: TextView, enabled: Boolean) {
    if( enabled ) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

