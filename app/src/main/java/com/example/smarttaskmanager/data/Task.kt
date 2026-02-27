package com.example.smarttaskmanager.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Date

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val priority: Priority,
    val dueDate: Long, // Store a timeStamp
    val isCompleted: Boolean = false,
    val reminderTime: Date? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastSynced: Long? = null
) : Parcelable {
    val formattedDueDate : String

        get () {
            val dateTime = Instant.ofEpochMilli(dueDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
            return dateTime.format(formatter)
        }

    val isOverdue : Boolean
        get() = !isCompleted && dueDate < System.currentTimeMillis()
}

enum class Priority {
    LOW, MEDIUM, HIGH, URGENT ;

    fun getColor(): Int = when (this) {
        LOW -> android.graphics.Color.rgb(76, 175, 80)    // Green
        MEDIUM -> android.graphics.Color.rgb(255, 193, 7)  // Amber
        HIGH -> android.graphics.Color.rgb(255, 87, 34)    // Deep Orange
        URGENT -> android.graphics.Color.rgb(244, 67, 54)   // Red
    }
}