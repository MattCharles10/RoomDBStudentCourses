package com.example.smarttaskmanager.workers


import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.smarttaskmanager.data.TaskDatabase
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val database = TaskDatabase.getDatabase(applicationContext)
            val now = Date()
            val oneHourFromNow = Date(now.time + TimeUnit.HOURS.toMillis(1))

            // Find tasks due in the next hour
            val upcomingTasks = database.taskDao()
                .getTasksDueBetween(now, oneHourFromNow)

            upcomingTasks.forEach { task ->
                if (task.reminderTime != null && !task.isCompleted) {
                    scheduleReminder(task.id, task.title)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Error processing reminders", e)
            Result.retry()
        }
    }

    private fun scheduleReminder(taskId: Long, taskTitle: String) {
        // Implementation of reminder scheduling
    }

    companion object {
        fun schedulePeriodicReminderCheck(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
                15, TimeUnit.MINUTES,
                5, TimeUnit.MINUTES // Flex interval
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    1, TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "reminder_check",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
        }
    }
}