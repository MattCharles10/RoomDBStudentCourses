package com.example.smarttaskmanager.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = :isCompleted ORDER BY dueDate ASC")
    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY dueDate ASC")
    fun getTasksByPriority(priority: Priority): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :start AND :end")
    suspend fun getTasksDueBetween(start: Long, end: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<Task>

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE isCompleted = 1 AND dueDate < :date")
    suspend fun deleteOldCompletedTasks(date: Long)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    fun getPendingTasksCount(): Flow<Int>
}