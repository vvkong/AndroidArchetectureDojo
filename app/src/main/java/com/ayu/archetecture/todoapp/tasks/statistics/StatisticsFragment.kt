package com.ayu.archetecture.todoapp.tasks.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ayu.archetecture.todoapp.databinding.FragmentTasksStatisticsBinding

/**
 * Author: allenwang
 * Date: 2021/9/9 15:31
 * Description:
 */
class StatisticsFragment: Fragment() {
    private lateinit var binding: FragmentTasksStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksStatisticsBinding.inflate(inflater)
        return binding.root
    }
}