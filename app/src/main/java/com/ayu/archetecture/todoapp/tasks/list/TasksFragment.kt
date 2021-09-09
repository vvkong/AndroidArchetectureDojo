package com.ayu.archetecture.todoapp.tasks.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ayu.archetecture.todoapp.databinding.FragmentTasksListBinding

/**
 * Author: allenwang
 * Date: 2021/9/9 14:17
 * Description:
 */
class TasksFragment: Fragment() {
    private lateinit var binding: FragmentTasksListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksListBinding.inflate(inflater)
        return binding.root
    }

}