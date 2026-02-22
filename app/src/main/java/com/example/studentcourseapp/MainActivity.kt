package com.example.studentcourseapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcourseapp.data.entities.Student
import com.example.studentcourseapp.ui.StudentAdapter
import com.example.studentcourseapp.viewmodel.SchoolViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SchoolViewModel
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[SchoolViewModel::class.java]

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.editSearch)
        val btnAddStudent = findViewById<Button>(R.id.btnAddStudent)
        val btnAddCourse = findViewById<Button>(R.id.btnAddCourse)
        val btnViewStudents = findViewById<Button>(R.id.btnViewStudents)
        val btnViewCourses = findViewById<Button>(R.id.btnViewCourses)

        // Setup RecyclerView
        studentAdapter = StudentAdapter(
            students = emptyList(),
            onItemClick = { student -> showStudentDetails(student) },
            onEnrollClick = { student -> showEnrollDialog(student) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter

        // Observe students
        viewModel.allStudents.observe(this) { students ->
            studentAdapter.updateStudents(students)
        }

        // Setup search
        searchEditText.setOnKeyListener { _, _, _ ->
            searchStudents(searchEditText.text.toString())
            true
        }

        // Button click listeners
        btnAddStudent.setOnClickListener { showAddStudentDialog() }
        btnAddCourse.setOnClickListener { showAddCourseDialog() }
        btnViewStudents.setOnClickListener {
            viewModel.allStudents.observe(this) { students ->
                studentAdapter.updateStudents(students)
            }
        }
        btnViewCourses.setOnClickListener { showCoursesDialog() }
    }

    private fun searchStudents(query: String) {
        if (query.isEmpty()) {
            viewModel.allStudents.observe(this) { students ->
                studentAdapter.updateStudents(students)
            }
        } else {
            viewModel.searchStudents(query).observe(this) { students ->
                studentAdapter.updateStudents(students)
            }
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.editName)
        val emailInput = dialogView.findViewById<EditText>(R.id.editEmail)
        val phoneInput = dialogView.findViewById<EditText>(R.id.editPhone)

        AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val email = emailInput.text.toString()
                val phone = phoneInput.text.toString()

                if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                    viewModel.addStudent(name, email, phone)
                    Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddCourseDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_course, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.editCourseName)
        val codeInput = dialogView.findViewById<EditText>(R.id.editCourseCode)
        val creditsInput = dialogView.findViewById<EditText>(R.id.editCredits)
        val instructorInput = dialogView.findViewById<EditText>(R.id.editInstructor)

        AlertDialog.Builder(this)
            .setTitle("Add New Course")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val code = codeInput.text.toString()
                val credits = creditsInput.text.toString().toIntOrNull() ?: 3
                val instructor = instructorInput.text.toString()

                if (name.isNotEmpty() && code.isNotEmpty() && instructor.isNotEmpty()) {
                    viewModel.addCourse(name, code, credits, instructor)
                    Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showStudentDetails(student: Student) {
        AlertDialog.Builder(this)
            .setTitle(student.name)
            .setMessage("Email: ${student.email}\nPhone: ${student.phone}")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showEnrollDialog(student: Student) {
        CoroutineScope(Dispatchers.Main).launch {
            val courses = viewModel.allCourses.value ?: emptyList()
            if (courses.isEmpty()) {
                Toast.makeText(this@MainActivity, "No courses available", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val courseNames = courses.map { it.courseName }.toTypedArray()

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Enroll ${student.name}")
                .setItems(courseNames) { _, which ->
                    val course = courses[which]
                    viewModel.enrollStudentInCourse(student.studentId, course.courseId)
                    Toast.makeText(
                        this@MainActivity,
                        "Enrolled in ${course.courseName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showCoursesDialog() {
        CoroutineScope(Dispatchers.Main).launch {
            val courses = viewModel.allCourses.value ?: emptyList()
            if (courses.isEmpty()) {
                Toast.makeText(this@MainActivity, "No courses available", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val courseList = courses.joinToString("\n") {
                "${it.courseName} (${it.courseCode}) - ${it.credits} credits\nInstructor: ${it.instructor}"
            }

            AlertDialog.Builder(this@MainActivity)
                .setTitle("All Courses")
                .setMessage(courseList)
                .setPositiveButton("OK", null)
                .show()
        }
    }
}