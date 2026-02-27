package com.example.smarttaskmanager.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.smarttaskmanager.data.Priority
import com.example.smarttaskmanager.data.Task
import com.example.smarttaskmanager.data.TaskDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TaskListViewModel(
    private val taskDao: TaskDao
) : ViewModel() {

    // StateFlow for UI state
    private val _uiState = MutableStateFlow(TaskListState())
    val uiState: StateFlow<TaskListState> = _uiState.asStateFlow()

    // LiveData example for comparison
    val allTasksLiveData = taskDao.getAllTasks().asLiveData()

    // Filtered tasks using Flow
    val filteredTasks = taskDao.getAllTasks()
        .combine(_uiState) { tasks, state ->
            filterAndSortTasks(tasks, state)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun filterAndSortTasks(tasks: List<Task>, state: TaskListState): List<Task> {
        return tasks
            .filter { task ->
                when (state.filter) {
                    TaskFilter.ALL -> true
                    TaskFilter.ACTIVE -> !task.isCompleted
                    TaskFilter.COMPLETED -> task.isCompleted
                    TaskFilter.HIGH_PRIORITY -> task.priority >= Priority.HIGH
                }
            }
            .sortedWith(
                when (state.sortBy) {
                    SortBy.DATE -> compareBy<Task> { it.dueDate }
                    SortBy.PRIORITY -> compareByDescending<Task> { it.priority }
                    SortBy.TITLE -> compareBy { it.title }
                }
            )
    }

    fun updateFilter(filter: TaskFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun updateSort(sortBy: SortBy) {
        _uiState.update { it.copy(sortBy = sortBy) }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    data class TaskListState(
        val filter: TaskFilter = TaskFilter.ALL,
        val sortBy: SortBy = SortBy.DATE,
        val isLoading: Boolean = false,
        val error: String? = null
    )
}

enum class TaskFilter { ALL, ACTIVE, COMPLETED, HIGH_PRIORITY }
enum class SortBy { DATE, PRIORITY, TITLE }