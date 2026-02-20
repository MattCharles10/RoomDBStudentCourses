package com.example.studentcourseapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "students")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val  course: Long = 0 ,
    val courseName : String,
    val courseCode : String,
    val credits : Int,
    val instructor : String
)
