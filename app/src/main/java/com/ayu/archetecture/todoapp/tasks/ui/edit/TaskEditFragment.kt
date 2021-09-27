package com.ayu.archetecture.todoapp.tasks.ui.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ayu.archetecture.todoapp.databinding.FragmentTaskEditBinding
import com.ayu.archetecture.todoapp.tasks.ui.ADD_EDIT_RESULT_OK
import com.ayu.archetecture.todoapp.tasks.ui.utils.EventObserver
import com.ayu.archetecture.todoapp.tasks.ui.utils.getViewModelFactory
import com.ayu.archetecture.todoapp.tasks.ui.utils.setupRefreshLayout
import com.ayu.archetecture.todoapp.tasks.ui.utils.setupSnackBar
import com.google.android.material.snackbar.Snackbar

/**
 * Author: allenwang
 * Date: 2021/9/26 17:11
 * Description: 任务编辑界面
 */
class TaskEditFragment: Fragment() {

    private val args: TaskEditFragmentArgs by navArgs()
    private lateinit var binding: FragmentTaskEditBinding
    private val viewModel by viewModels<TaskEditViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskEditBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setupSnackBar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        setupNavigation()
        setupRefreshLayout(binding.swipeRefreshLayout)

        viewModel.start(args.taskId)
    }

    private fun setupNavigation() {
        viewModel.taskUpdateEvent.observe(viewLifecycleOwner, EventObserver {
            val directions = TaskEditFragmentDirections.actionNavTaskEditToList(ADD_EDIT_RESULT_OK)
            findNavController().navigate(directions)
        })
    }
}