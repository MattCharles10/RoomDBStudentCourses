package com.example.studentcourseapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


// One Side of One-to-Many

@Entity("students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val studentId : Long = 0,
    val name : String ,
    val email: String ,
    val phone: String ,
    val enrollmentDate : Long = System.currentTimeMillis()

)
