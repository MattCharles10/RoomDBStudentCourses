package com.example.studentcourseapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studentcourseapp.data.daos.CourseDao
import com.example.studentcourseapp.data.daos.EnrollmentDao
import com.example.studentcourseapp.data.daos.StudentsDao
import com.example.studentcourseapp.data.entities.Course
import com.example.studentcourseapp.data.entities.Enrollment
import com.example.studentcourseapp.data.entities.Student


@Database(
    entities = [Student::class, Course::class, Enrollment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentsDao
    abstract fun courseDao(): CourseDao
    abstract fun enrollmentDao(): EnrollmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_course_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}