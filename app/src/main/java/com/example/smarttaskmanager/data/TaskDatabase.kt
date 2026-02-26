package com.example.smarttaskmanager.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


abstract class TaskDatabase : RoomDatabase(){
    abstract fun taskDao() : TaskDao

    companion object{

        @Volatile
        private  var INSTANCE : TaskDatabase ? = null

        fun getInstance(context : Context) : TaskDatabase{
          return INSTANCE ?: synchronized(this){
              val instance = Room.databaseBuilder(
                  context.applicationContext,
                  TaskDatabase::class.java,
                  "task_database"
              ).build()
              INSTANCE = instance
              instance
          }
        }


    }
}