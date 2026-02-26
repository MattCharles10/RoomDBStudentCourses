package com.example.smarttaskmanager.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskListViewModel(
    application : Application,
    private val savedStateHandle : SavedStateHandle
) : AndroidViewModel(application) {

    // StateFlow for UI State
    private val _uiState = MutableStateFlow(TaskListState())
    val uiState : StateFlow<TaskListState> = _uiState

    //Live Data for tasks
    val pendingTasks = taskDao.getPendingTasks()

    init{
        loadTasks()

    }
}