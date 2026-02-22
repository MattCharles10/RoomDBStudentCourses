package com.example.studentcourseapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcourseapp.data.database.AppDatabase
import com.example.studentcourseapp.data.entities.Course
import com.example.studentcourseapp.data.entities.Student
import com.example.studentcourseapp.data.relationships.CourseWithStudents
import com.example.studentcourseapp.data.relationships.StudentWithCourses
import com.example.studentcourseapp.repository.SchoolRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository

    val allStudents: Flow<List<Student>>
    val allCourses: Flow<List<Course>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SchoolRepository(
            database.studentDao(),
            database.courseDao(),
            database.enrollmentDao()
        )

        allStudents = repository.getAllStudents()
        allCourses = repository.getAllCourses()
    }

    fun addStudent(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val student = Student(
                name = name,
                email = email,
                phone = phone
            )
            repository.addStudent(student)
        }
    }

    fun addCourse(courseName: String, courseCode: String, credits: Int, instructor: String) {
        viewModelScope.launch {
            val course = Course(
                courseName = courseName,
                courseCode = courseCode,
                credits = credits,
                instructor = instructor
            )
            repository.addCourse(course)
        }
    }

    fun enrollStudentInCourse(studentId: Long, courseId: Long) {
        viewModelScope.launch {
            repository.enrollStudent(studentId, courseId)
        }
    }

    fun withdrawStudentFromCourse(studentId: Long, courseId: Long) {
        viewModelScope.launch {
            repository.withdrawStudent(studentId, courseId)
        }
    }

    fun getStudentWithCourses(studentId: Long): Flow<StudentWithCourses?> =
        repository.getStudentWithCourses(studentId)

    fun getCourseWithStudents(courseId: Long): Flow<CourseWithStudents?> =
        repository.getCourseWithStudents(courseId)

    fun searchStudents(query: String): Flow<List<Student>> = repository.searchStudents(query)

    fun getEnrollmentCountForCourse(courseId: Long): Flow<Int> =
        repository.getEnrollmentCountForCourse(courseId)
}