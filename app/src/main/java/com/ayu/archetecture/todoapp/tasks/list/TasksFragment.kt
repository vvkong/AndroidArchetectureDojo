package com.ayu.archetecture.todoapp.tasks.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ayu.archetecture.todoapp.R
import com.ayu.archetecture.todoapp.databinding.FragmentTasksListBinding
import com.ayu.archetecture.todoapp.tasks.utils.EventObserver
import com.ayu.archetecture.todoapp.tasks.utils.getViewModelFactory
import com.ayu.archetecture.todoapp.tasks.utils.setupRefreshLayout
import com.ayu.archetecture.todoapp.tasks.utils.setupSnackBar
import com.google.android.material.snackbar.Snackbar

/**
 * Author: allenwang
 * Date: 2021/9/9 14:17
 * Description: 任务列表界面
 */
class TasksFragment: Fragment() {

    // 延迟实例化：委托的强大
    private val viewModel: TasksViewModel by viewModels<TasksViewModel>() {
        getViewModelFactory()
    }

    // 接收nav_graph的参数
    private val args: TasksFragmentArgs by navArgs<TasksFragmentArgs>()

    private lateinit var binding: FragmentTasksListBinding

    private lateinit var listAdapter: TasksListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTasksListBinding.inflate(inflater, container, false)
            .apply { vm = viewModel }
//        binding.vm  = viewModel
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackBar()
        setupListAdapter()
        setupRefreshLayout(binding.swipeRefreshLayout, binding.taskList)
        setupNavigation()
    }

    private fun setupSnackBar() {
        view?.setupSnackBar(viewLifecycleOwner, viewModel.snackbarText, Snackbar.LENGTH_LONG)
    }

    private fun setupListAdapter() {
        val vm = binding.vm
        vm?.also {
            listAdapter = TasksListAdapter(it)
            binding.taskList.adapter = listAdapter
        }
    }

    private fun setupNavigation() {
        viewModel.openAddNewTaskEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToAddNewTask()
        })
    }

    private fun navigateToAddNewTask() {
        viewModel.showSnackbar(R.string.navigate_to_add_new_task)
        // TODO
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = inflater.inflate(R.menu.tasks_fragment_menu, menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.menu_filter -> {
            viewModel.showFilteringPopupMenu()
            true
        }
        R.id.menu_clear -> {
            viewModel.clearCompletedTasks()
            true
        }
        R.id.menu_refresh -> {
            viewModel.loadTasks(true)
            true
        }
        else -> false
    }
}