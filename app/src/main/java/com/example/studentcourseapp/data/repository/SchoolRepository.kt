package com.example.studentcourseapp.data.repository

import com.example.studentcourseapp.data.daos.*
import com.example.studentcourseapp.data.entities.*
import com.example.studentcourseapp.data.relationships.CourseWithStudents
import com.example.studentcourseapp.data.relationships.StudentWithCourses
import kotlinx.coroutines.flow.Flow



class SchoolRepository(
    private val studentDao: StudentsDao,
    private val courseDao: CourseDao,
    private val enrollmentDao: EnrollmentDao
) {

    // Student operations
    suspend fun addStudent(student: Student): Long = studentDao.insertStudents(student)
    suspend fun updateStudent(student: Student) = studentDao.updateStudent(student)
    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)
    fun getAllStudents(): Flow<List<Student>> = studentDao.getAllStudents()
    fun searchStudents(query: String): Flow<List<Student>> = studentDao.searchStudents(query)
    fun getStudentWithCourses(studentId: Long): Flow<StudentWithCourses?> =
        studentDao.getStudentWithCourse(studentId)

    // Course operations
    suspend fun addCourse(course: Course): Long = courseDao.insertCourse(course)
    suspend fun updateCourse(course: Course) = courseDao.updateCourse(course)
    suspend fun deleteCourse(course: Course) = courseDao.deleteCourse(course)
    fun getAllCourses(): Flow<List<Course>> = courseDao.getAllCourse()
    fun getCourseWithStudents(courseId: Long): Flow<CourseWithStudents?> =
        courseDao.getCourseWithStudents(courseId)

    // Enrollment operations
    suspend fun enrollStudent(studentId: Long, courseId: Long) {
        val enrollment = Enrollment(
            student = studentId,
            courseId = courseId
        )
        enrollmentDao.enrollStudent(enrollment)
    }

    suspend fun withdrawStudent(studentId: Long, courseId: Long) {
        val enrollment = enrollmentDao.getEnrollment(studentId, courseId)
        enrollment?.let {
            enrollmentDao.withdrawStudent(it)
        }
    }

    suspend fun updateGrade(enrollmentId: Long, grade: String) {
        enrollmentDao.updateGrade(enrollmentId, grade)
    }

    fun getEnrollmentCountForCourse(courseId: Long): Flow<Int> =
        enrollmentDao.getEnrollmentCountForCourse(courseId)

    suspend fun isStudentEnrolled(studentId: Long, courseId: Long): Boolean {
        return enrollmentDao.getEnrollment(studentId, courseId) != null
    }
}