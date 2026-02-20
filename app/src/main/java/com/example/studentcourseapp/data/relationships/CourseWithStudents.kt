package com.example.studentcourseapp.data.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.studentcourseapp.data.entities.Course
import com.example.studentcourseapp.data.entities.Enrollment
import com.example.studentcourseapp.data.entities.Student


data class CourseWithStudents(

    @Embedded val course: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "studentId",
        associateBy = Junction(Enrollment::class)
    )
    val students: List<Student>
)