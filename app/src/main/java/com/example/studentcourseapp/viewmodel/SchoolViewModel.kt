package com.example.studentcourseapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.studentcourseapp.data.database.AppDatabase
import com.example.studentcourseapp.data.entities.Course
import com.example.studentcourseapp.data.entities.Student
import com.example.studentcourseapp.repository.SchoolRepository
import kotlinx.coroutines.launch

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository

    // Use MutableLiveData internally
    private val _allStudents = MutableLiveData<List<Student>>()
    val allStudents: LiveData<List<Student>> get() = _allStudents

    private val _allCourses = MutableLiveData<List<Course>>()
    val allCourses: LiveData<List<Course>> get() = _allCourses

    private val _searchResults = MutableLiveData<List<Student>>()
    val searchResults: LiveData<List<Student>> get() = _searchResults

    init {
        val database = AppDatabase.getDatabase(application)
        repository = SchoolRepository(
            database.studentDao(),
            database.courseDao(),
            database.enrollmentDao()
        )

        // Load initial data
        loadAllStudents()
        loadAllCourses()
    }

    private fun loadAllStudents() {
        viewModelScope.launch {
            repository.getAllStudents().collect { students ->
                _allStudents.postValue(students)
            }
        }
    }

    private fun loadAllCourses() {
        viewModelScope.launch {
            repository.getAllCourses().collect { courses ->
                _allCourses.postValue(courses)
            }
        }
    }

    fun addStudent(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val student = Student(
                name = name,
                email = email,
                phone = phone
            )
            repository.addStudent(student)
            // Data will auto-refresh through Flow collection
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
            // Data will auto-refresh through Flow collection
        }
    }

    fun enrollStudentInCourse(studentId: Long, courseId: Long) {
        viewModelScope.launch {
            repository.enrollStudent(studentId, courseId)
        }
    }

    fun searchStudents(query: String): LiveData<List<Student>> {
        viewModelScope.launch {
            repository.searchStudents(query).collect { students ->
                _searchResults.postValue(students)
            }
        }
        return _searchResults
    }
}