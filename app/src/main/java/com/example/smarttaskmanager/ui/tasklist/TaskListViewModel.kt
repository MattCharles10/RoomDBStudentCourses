package com.example.smarttaskmanager.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.smarttaskmanager.data.Task
import com.example.smarttaskmanager.data.TaskDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TaskListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val database = TaskDatabase.getInstance(application)
    private val taskDao = database.taskDao()

    // StateFlow for UI state (survives configuration changes)
    private val _uiState = MutableStateFlow(TaskListState())
    val uiState: StateFlow<TaskListState> = _uiState

    // LiveData for tasks (can also use Flow)
    val pendingTasks = taskDao.getPendingTasks()

    init {
        loadTasks()
        // Restore last selected filter from saved state (handles process death)
        savedStateHandle.get<String>("filter")?.let { filter ->
            _uiState.value = _uiState.value.copy(filter = filter)
        }
    }

    private fun loadTasks() {
        viewModelScope.launch {
            taskDao.getAllTasks()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message
                    )
                }
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            taskDao.updateTask(updatedTask)

            // Save last action to saved state (handles process death)
            savedStateHandle.set("last_action", "Toggled: ${task.title}")
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Save current filter to saved state
        savedStateHandle.set("filter", _uiState.value.filter)
    }
}

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val filter: String = "all"
)