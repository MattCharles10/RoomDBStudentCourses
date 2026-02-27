package com.example.smarttaskmanager.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.smarttaskmanager.MainActivity
import com.example.smarttaskmanager.R
import com.example.smarttaskmanager.data.TaskDatabase
import kotlinx.coroutines.*

class TaskSyncService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val NOTIFICATION_ID = 1001
    private val CHANNEL_ID = "task_sync_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            syncTasks()
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private suspend fun syncTasks() {
        val database = TaskDatabase.getDatabase(applicationContext)
        // Simulate cloud sync
        delay(5000) // Simulate network delay

        // Update last synced time for all tasks
        database.taskDao().getAllTasks().collect { tasks ->
            tasks.forEach { task ->
                database.taskDao().updateTask(
                    task.copy(lastSynced = java.util.Date())
                )
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Task Sync Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Task Sync")
            .setContentText("Syncing your tasks...")
            .setSmallIcon(R.drawable.ic_sync)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
