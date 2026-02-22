package com.example.studentcourseapp.data.daos

import androidx.room.*
import com.example.studentcourseapp.data.entities.Enrollment
import kotlinx.coroutines.flow.Flow

@Dao
interface EnrollmentDao {

    @Insert
    suspend fun enrollStudent(enrollment: Enrollment): Long

    @Delete
    suspend fun withdrawStudent(enrollment: Enrollment)


    @Query("SELECT * FROM enrollments WHERE studentId = :studentId AND courseId = :courseId")
    suspend fun getEnrollment(studentId: Long, courseId: Long): Enrollment?


    @Query("SELECT COUNT(*) FROM enrollments WHERE courseId = :courseId")
    fun getEnrollmentCountForCourse(courseId: Long): Flow<Int>

    @Query("SELECT * FROM enrollments WHERE studentId = :studentId")
    fun getEnrollmentsForStudent(studentId: Long): Flow<List<Enrollment>>


    @Query("UPDATE enrollments SET grade = :grade WHERE enrollmentId = :enrollmentId")
    suspend fun updateGrade(enrollmentId: Long, grade: String)
}