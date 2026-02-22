package com.example.studentcourseapp.data.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.studentcourseapp.data.entities.Course
import com.example.studentcourseapp.data.relationships.CourseWithStudents
import kotlinx.coroutines.flow.Flow

interface CourseDao {


    @Insert
    suspend fun  insertCourse(course : Course) : Long

    @Update
    suspend fun  updateCourse(course : Course)

    @Delete
    suspend fun deleteCourse(course: Course)

    @Query("SELECT * FROM courses ORDER BY courseName ASC")
    fun getAllCourse(): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    suspend fun getCourseById(courseId: Long): Course?

    @Transaction
    @Query("SELECT * FROM courses WHERE courseId = :courseId")
    fun getCourseWithStudents(courseId: Long): Flow<CourseWithStudents?>

}