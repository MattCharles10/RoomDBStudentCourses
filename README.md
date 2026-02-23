# 📚 Room Database Learning Repository  
## Student Course Management App

A comprehensive Android application built to demonstrate **Room Database concepts** through a practical Student Course Management System.

This repository is designed for developers who want to learn Room Database from basics to advanced concepts.

---

## 🎯 Purpose

This repository serves as a complete learning resource for understanding Room Database in Android. It covers everything from basic CRUD operations to complex relationships between tables.

---

## 📚 Additional Resources

- [Room Database Documentation](https://developer.android.com/training/data-storage/room)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Material Design Guidelines](https://m3.material.io/)


---

## 📖 My Learning

Through building this Student Course Management App, I gained hands-on experience with the following Room Database concepts:

### 🔰 Beginner Level
- Set up Room Database in an Android project from scratch  
- Created Entity classes with proper annotations  
- Implemented Data Access Objects (DAOs)  
- Performed CRUD operations (Insert, Update, Delete, Query)  
- Integrated LiveData with Room  
- Understood and implemented basic database migrations  

### 📊 Intermediate Level
- Implemented One-to-Many relationships (Student → Enrollments)  
- Built Many-to-Many relationships using a Junction table (Students ↔ Courses)  
- Applied Foreign Key constraints with CASCADE delete  
- Wrote complex SQL queries using JOIN  
- Used Kotlin Flow for reactive data updates  
- Implemented Type Converters for custom data types  

### 🚀 Advanced Level
- Worked with database transactions  
- Learned how to use pre-populated databases  
- Integrated Room with Kotlin Coroutines  
- Wrote unit tests for Room database  
- Handled schema changes with migrations  
- Optimized performance using indices  

---

## 🏗️ Project Structure

```text
app/
├── src/main/java/com/example/studentcourseapp/
│   ├── data/
│   │   ├── entities/
│   │   │   ├── Student.kt
│   │   │   ├── Course.kt
│   │   │   └── Enrollment.kt
│   │   ├── daos/
│   │   │   ├── StudentDao.kt
│   │   │   ├── CourseDao.kt
│   │   │   └── EnrollmentDao.kt
│   │   ├── database/
│   │   │   └── AppDatabase.kt
│   │   └── relationships/
│   │       ├── StudentWithCourses.kt
│   │       └── CourseWithStudents.kt
│   ├── repository/
│   │   └── SchoolRepository.kt
│   ├── viewmodel/
│   │   └── SchoolViewModel.kt
│   └── ui/
│       ├── MainActivity.kt
│       └── StudentAdapter.kt
│
└── src/main/res/
    ├── layout/
    │   ├── activity_main.xml
    │   ├── item_student.xml
    │   ├── dialog_add_student.xml
    │   └── dialog_add_course.xml
    ├── drawable/
    │   ├── ic_splash_icon.xml
    │   └── splash_background.xml
    └── values/
        ├── themes.xml
        └── colors.xml
