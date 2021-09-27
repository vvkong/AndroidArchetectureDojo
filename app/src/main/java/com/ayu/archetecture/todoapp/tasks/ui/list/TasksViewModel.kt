package com.ayu.archetecture.todoapp.tasks.ui.list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.TasksRepository
import com.ayu.archetecture.todoapp.R
import com.ayu.archetecture.todoapp.tasks.ui.ADD_EDIT_RESULT_OK
import com.ayu.archetecture.todoapp.tasks.ui.utils.Event
import kotlinx.coroutines.*

/**
 * Author: allenwang
 * Date: 2021/9/13 14:31
 * Description:
 */
class TasksViewModel constructor(
    private val tasksRepository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
    ): ViewModel() {

    fun refresh() {
        _forceUpdate.value = true
    }

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _items: LiveData<List<Task>> = _forceUpdate.switchMap { forceUpdate ->
        if( forceUpdate ) {
            _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.refreshTasks()
                _dataLoading.value = false
            }
        }
        tasksRepository.observeTasks().distinctUntilChanged().switchMap { filterTasks(it) }
    }
    val items: LiveData<List<Task>> = _items

    private val isDataLoadingError = MutableLiveData<Boolean>()
    private fun filterTasks(tasks: Result<List<Task>>): LiveData<List<Task>> {
        val result =  MutableLiveData<List<Task>>()
        if( tasks is Result.Success ) {
            isDataLoadingError.value = false
            viewModelScope.launch { result.value = filterItems(tasks.data, getSavedFilterType()) }
        } else {
            result.value = emptyList()
            isDataLoadingError.value = true
        }
        return result
    }

    private fun  filterItems(tasks: List<Task>, filteringType: TaskFilterType): List<Task> {
        val tasksToShow = ArrayList<Task>()
        for( task in tasks ) {
            when( filteringType ) {
                TaskFilterType.ALL_TASKS -> tasksToShow.add(task)
                TaskFilterType.ACTIVE_TASKS -> if (task.isActive ) tasksToShow.add(task)
                TaskFilterType.COMPLETED_TASKS -> if (task.isCompleted) tasksToShow.add(task)
            }
        }
        return tasksToShow
    }

    val empty = Transformations.map(_items) {
        it.isEmpty()
    }

    // 对内可变，对外不可修改
    private val _dataLoading = MutableLiveData<Boolean>(false)
    val dataLoading: LiveData<Boolean> = _dataLoading;

    private val _resFilterLabel = MutableLiveData<Int>()
    val resFilterLabel: LiveData<Int> = _resFilterLabel

    private val _resNoTaskLabel = MutableLiveData<Int>()
    val resNoTaskLabel:LiveData<Int> = _resNoTaskLabel

    private val _resNoTaskIcon = MutableLiveData<Int>()
    val resNoTaskIcon: LiveData<Int> = _resNoTaskIcon

    private val _btnAddTaskVisible = MutableLiveData<Boolean>()
    val btnAddTaskVisible: LiveData<Boolean> = _btnAddTaskVisible

    private fun getSavedFilterType(): TaskFilterType = savedStateHandle.get(TASKS_FILTER_SAVED_STATE_TYPE) ?: TaskFilterType.ALL_TASKS

    private fun setFilter(type: TaskFilterType) {
        savedStateHandle.set(TASKS_FILTER_SAVED_STATE_TYPE, type)
        when(type) {
            TaskFilterType.ALL_TASKS -> setFilter(
                R.string.label_all,
                R.string.label_no_task,
                R.drawable.logo_no_fill,
            true)
            TaskFilterType.ACTIVE_TASKS -> setFilter(
                R.string.label_active,
                R.string.label_no_active,
                R.drawable.ic_check_circle_96dp,
                false
            )
            TaskFilterType.COMPLETED_TASKS -> setFilter(
                R.string.label_completed,
                R.string.label_no_completed,
                R.drawable.ic_verified_user_96dp,
                false
            )
        }
    }
    private fun setFilter(
        @StringRes filterLabelString: Int,
        @StringRes noTasksLabelString: Int,
        @DrawableRes noTasksIconDrawable: Int,
        btnAddTaskVisible: Boolean) {
        _resFilterLabel.value = filterLabelString
        _resNoTaskIcon.value = noTasksIconDrawable
        _resNoTaskLabel.value = noTasksLabelString
        _btnAddTaskVisible.value = btnAddTaskVisible
    }

    // 通过事件驱动snackbar展示
    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun showSnackbar(@StringRes resText: Int) {
        _snackbarText.value = Event(resText)
    }

    private val _openAddNewTaskEvent = MutableLiveData<Event<Unit>>()
    val openAddNewTaskEvent: LiveData<Event<Unit>> = _openAddNewTaskEvent
    fun navigateToAddNewTask() {
        _openAddNewTaskEvent.value = Event(Unit)
    }

    private val _openTaskEvent = MutableLiveData<Event<String>>()
    val openTaskEvent: LiveData<Event<String>> = _openTaskEvent
    fun openTask(taskId: String) {
        _openTaskEvent.value = Event(taskId)
    }

    fun completeTask(task: Task, complete: Boolean) {
        viewModelScope.launch {
            tasksRepository?.let {
                if( complete ) {
                    it.completeTask(task)
                } else {
                    it.activateTask(task)
                }
        } }
    }

    fun showFilteringPopupMenu() {
        _snackbarText.value = Event(R.string.show_filtering_popup_menu)
    }

    fun clearCompletedTasks() {
        _snackbarText.value = Event(R.string.clear_completed_tasks)
    }

    fun loadTasks(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    var resultMessageShown = false
    fun showResultMessage(result: Int) {
        if (resultMessageShown) return
        when (result) {
            //EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_task_message)
            ADD_EDIT_RESULT_OK -> showSnackbar(R.string.successfully_added_task_message)
            //DELETE_RESULT_OK -> showSnackbarMessage(R.string.successfully_deleted_task_message)
        }
        resultMessageShown = true
    }

    /**
     * 踩坑说明：这里依赖了类成员变量的初始化顺序
     * 如把init放到setFilter、loadTasks函数中使用到的成员函数前面，则会出现空指针
     * init调用比后续的成员变量初始化优先执行
     */
    init {
        setFilter(getSavedFilterType())
        loadTasks(true)
    }
}


enum class TaskFilterType {
    ALL_TASKS,
    ACTIVE_TASKS,
    COMPLETED_TASKS
}

const val TASKS_FILTER_SAVED_STATE_TYPE = "TASKS_FILTER_SAVED_STATE_TYPE"