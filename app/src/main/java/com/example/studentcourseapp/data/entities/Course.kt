package com.example.studentcourseapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val courseId: Long = 0,

    val courseName: String,
    val courseCode: String,
    val credits: Int,
    val instructor: String
)
