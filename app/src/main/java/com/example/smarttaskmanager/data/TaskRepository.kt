package com.example.smarttaskmanager.data

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository  @Inject constructor(
    private val taskDao : TaskDao
){
    fun getAllTasks() : Flow<List<Task>> = taskDao.getAllTasks()

    fun getPendingTask() : Flow<List<Task>> = taskDao.getTasksByCompletion(false)

    fun getCompletedTasks() : Flow<List<Task>> = taskDao.getTasksByCompletion(true)

    fun getTaskById(taskId : Long) : Flow<Task> = taskDao.getTaskById(taskId)

    suspend fun insertTask(task : Task) : Long = taskDao.insertTask(task)

    suspend fun updateTask(task : Task) = taskDao.updateTask(task)
}