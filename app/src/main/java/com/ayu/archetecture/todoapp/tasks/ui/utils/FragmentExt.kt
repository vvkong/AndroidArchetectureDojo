package com.ayu.archetecture.todoapp.tasks.ui.utils

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ayu.archetecture.todoapp.R
import com.ayu.archetecture.todoapp.tasks.App
import com.ayu.archetecture.todoapp.tasks.ViewModelFactory
import com.ayu.archetecture.todoapp.tasks.ui.widget.ScrollChildSwipeRefreshLayout

/**
 * Author: allenwang
 * Date: 2021/9/23 18:01
 * Description: Fragment扩展方法
 */

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as App).tasksRepository
    return ViewModelFactory(repository, this, arguments)
}

fun Fragment.setupRefreshLayout(
    swipeRefreshLayout: ScrollChildSwipeRefreshLayout,
    scrollUpChild: View? = null ) {
    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(requireActivity(), R.color.design_default_color_primary),
        ContextCompat.getColor(requireActivity(), R.color.design_default_color_secondary),
        ContextCompat.getColor(requireActivity(), R.color.design_default_color_primary_dark))
    scrollUpChild?.let { swipeRefreshLayout.scrollUpChild = scrollUpChild}
}

