package com.example.studentcourseapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "enrollments",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Course::class,
            parentColumns = ["courseId"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["studentId", "courseId"], unique = true)
    ]
)
data class Enrollment(
    @PrimaryKey(autoGenerate = true)
    val enrollmentId: Long = 0,
    val studentId: Long,
    val courseId: Long,
    val enrollmentDate: Long = System.currentTimeMillis(),
    val grade: String? = null
)