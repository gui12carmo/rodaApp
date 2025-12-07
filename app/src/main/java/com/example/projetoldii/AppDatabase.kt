package com.example.projetoldii.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.ProjectDao
import com.example.projetoldii.TaskDao
import com.example.projetoldii.TaskTypeDao
import com.example.projetoldii.UserDao

@Database(
    entities = [User::class, Project::class, TaskType::class, Task::class, AddProgrammer::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskTypeDao(): TaskTypeDao
    abstract fun taskDao(): TaskDao
    abstract fun addProgrammerDao(): AddProgrammerDao
}
