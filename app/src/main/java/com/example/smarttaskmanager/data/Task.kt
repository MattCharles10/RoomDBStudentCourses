package com.example.smarttaskmanager.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val dueDate: Date,
    val isCompleted: Boolean = false,
    val reminderTime: Date? = null,
    val createdAt: Date = Date()
) : Parcelable

enum class Priority {
    HIGH, MEDIUM, LOW
}