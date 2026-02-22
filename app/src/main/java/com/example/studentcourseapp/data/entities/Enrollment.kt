package com.example.studentcourseapp.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "enrollment" ,
    foreignKeys = [
        ForeignKey(
            entity = Student :: class,
            parentColumns = ["studentsId"],
            childColumns = ["studentsId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Course :: class ,
            parentColumns = ["courseId"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["studentId" , "courseId"] , unique = true)]
)


data class Enrollment(

    @PrimaryKey(autoGenerate = true)
    val enrollment: Long = 0,
    val student : Long,
    val courseId : Long,
    val enrollmentDate: Long = System.currentTimeMillis(),
    val grade : String ?= null

)