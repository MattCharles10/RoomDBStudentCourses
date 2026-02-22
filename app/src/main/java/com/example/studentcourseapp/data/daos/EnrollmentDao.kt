package com.example.studentcourseapp.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.studentcourseapp.data.entities.Enrollment
import kotlinx.coroutines.flow.Flow


@Dao
interface EnrollmentDao {
    @Insert
    suspend fun enrollStudent(enrollment: Enrollment): Long

    @Delete
    suspend fun withdrawStudent(enrollment: Enrollment)

    @Query("SELECT * FROM enrollment WHERE student = :studentId AND courseId = :courseId")
    suspend fun getEnrollment(studentId: Long, courseId: Long): Enrollment?

    @Query("SELECT COUNT(*) FROM enrollment WHERE courseId = :courseId")
    fun getEnrollmentCountForCourse(courseId: Long): Flow<Int>

    @Query("SELECT * FROM enrollment WHERE student = :studentId")
    fun getEnrollmentsForStudent(studentId: Long): Flow<List<Enrollment>>

    @Query("UPDATE enrollment SET grade = :grade WHERE enrollmentDate = :enrollmentId")
    suspend fun updateGrade(enrollmentId: Long, grade: String)
}