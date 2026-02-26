package com.example.smarttaskmanager.ui.tasklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import com.example.smarttaskmanager.data.Priority
import com.example.smarttaskmanager.data.Task
import com.example.smarttaskmanager.data.TaskDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Date

class TaskDetailViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val database = TaskDatabase.getInstance(application)
    private val taskDao = database.taskDao()

    private val _task = MutableStateFlow<Task?>(null)
    val task : StateFlow<Task?> = _task

    private val _isSaving = MutableStateFlow(false)
    val isSaving : StateFlow<Boolean> = _isSaving

    fun loadTask(taskId : Long){
        viewModelScope.launch {
            val loadedTask = taskDao.getTaskById(taskId)
            _task.value = loadedTask
        }
    }

    fun saveTask(
        title : String ,
        description: String ,
        priority : Priority,
        dueDate: Date,
        remainderTime: Data?
    ){
        viewModelScope.launch {
            _isSaving.value = true
            try{
                val task = _task.value?.copy(
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    reminderTime = remainderTime
                ) ?: Task(
                    title = title,
                    description =  description,
                    priority = priority,
                    dueDate = dueDate,
                    reminderTime = remainderTime
                )

                if(task.id == 0L){
                    taskDao.insertTask(task)
                }else{
                    taskDao.updateTask(task)
                }

                remainderTime?.let{
                    scheduleReminder(task.id , it)
                }
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun scheduleReminder(taskId : Long , remainderTime: Data){
        val workManger = androidx.work.WorkManager.getInstance(getApplication())
    }
}