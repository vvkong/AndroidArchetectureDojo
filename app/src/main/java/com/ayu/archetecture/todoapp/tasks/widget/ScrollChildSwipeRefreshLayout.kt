package com.ayu.archetecture.todoapp.tasks.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Author: allenwang
 * Date: 2021/9/13 14:04
 * Description:
 * * 扩展 [SwipeRefreshLayout] 以支持非直接滑动子控件
 * [SwipeRefreshLayout] 在滑动控件作为直接子控件时才生效: 触发刷新仅当视图在顶部.
 * 此类的 setScrollUpChild 方法决定 哪个视图来控制此行为
 */
class ScrollChildSwipeRefreshLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
    ): SwipeRefreshLayout(context, attributeSet) {

    var scrollUpChild: View? = null;

    override fun canChildScrollUp() = scrollUpChild?.canScrollVertically(-1)?: super.canChildScrollUp()

}