package com.example.studentcourseapp.data.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.studentcourseapp.data.entities.Student
import com.example.studentcourseapp.data.relationships.StudentWithCourses

interface StudentsDao {

    @Insert
    suspend fun  insertStudents(student : Student) : Long

    @Update
    suspend fun  updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student : Student)

    @Query("SELECT * FROM students ORDER BY name ASC")
    fun getAllStudents() : Flow<List<Student>>


    @Query("SELECT * FROM students WHERE studentId = :studentId")
    fun getStudentWithCourse(studentId: Long) : Flow<StudentWithCourses?>

    @Query("SELECT * FROM students WHERE name LIKE '%' || :searchQuery || '%' OR email LIKE '%' || :searchQuery || '%'")
    fun searchStudents(searchQuery : String) : Flow<List<Student>>

}