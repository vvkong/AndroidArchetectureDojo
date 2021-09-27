package com.ayu.archetecture.todoapp.tasks.ui.edit

import android.text.TextUtils
import androidx.lifecycle.*
import com.ayu.archetecture.todoapp.R
import com.ayu.archetecture.todoapp.tasks.data.TasksRepository
import com.ayu.archetecture.todoapp.tasks.data.entity.Result
import com.ayu.archetecture.todoapp.tasks.data.entity.Task
import com.ayu.archetecture.todoapp.tasks.ui.utils.Event
import kotlinx.coroutines.launch

/**
 * Author: allenwang
 * Date: 2021/9/26 17:58
 * Description: 任务编辑界面
 */
class TaskEditViewModel(
    private val tasksRepository: TasksRepository,
    private val savedStateHandle: SavedStateHandle
    ): ViewModel() {

    private val _loading = MutableLiveData<Boolean>(false)
    var loading: LiveData<Boolean> = _loading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    var title = MutableLiveData<String>("")
    var description = MutableLiveData<String>("")

    private val _taskUpdateEvent = MutableLiveData<Event<Unit>>()
    val taskUpdateEvent: LiveData<Event<Unit>> = _taskUpdateEvent

    fun saveTask() {
        if( TextUtils.isEmpty(title.value) ) {
            _snackbarText.value = Event(R.string.title_not_allow_empty)
            return
        }
        viewModelScope.launch {
            val task = Task(title = title.value?:"", description = description.value?:"", isCompleted = false)
            taskId?.let { task.id = it }
            tasksRepository.saveTask(task)
            _taskUpdateEvent.value = Event(Unit)
        }
    }

    private var taskId: String? = null
    fun start(taskId: String? = null) {
        if(loading.value == true) {
            return
        }
        this.taskId = taskId
        taskId?.let {
            _loading.value = true
            viewModelScope.launch {
                when(val result = tasksRepository.getTask(it)) {
                    is Result.Success -> onLoadSuccess(result.data)
                    is Result.Error -> _snackbarText.value = Event(R.string.load_fail)
                    else -> Unit
                }
                _loading.value = false
            }
        }
    }

    private fun onLoadSuccess(task: Task) {
        taskId = task.id
        title.value = task.title
        description.value = task.description
    }
}