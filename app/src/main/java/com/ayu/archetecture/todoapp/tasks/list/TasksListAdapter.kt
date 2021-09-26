package com.ayu.archetecture.todoapp.tasks.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayu.archetecture.todoapp.databinding.ItemTaskListBinding
import com.ayu.archetecture.todoapp.tasks.data.entity.Task


/**
 * Author: allenwang
 * Date: 2021/9/24 15:10
 * Description: 任务列表适配器
 */
class TasksListAdapter constructor(
    private val viewModel: TasksViewModel
    ): ListAdapter<Task, TasksListAdapter.ViewHolder>(TasksDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(viewModel, task)
    }

    class ViewHolder(private val binding: ItemTaskListBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TasksViewModel, item: Task) {
            binding.vm = viewModel
            binding.task = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TasksDiffCallback: DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}