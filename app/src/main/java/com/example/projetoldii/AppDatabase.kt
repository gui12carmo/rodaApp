package com.example.projetoldii.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.ProjectDao
import com.example.projetoldii.TaskDao
import com.example.projetoldii.TaskTypeDao
import com.example.projetoldii.UserDao

@Database(
    entities = [User::class, Project::class, TaskType::class, Task::class, AddProgrammer::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskTypeDao(): TaskTypeDao
    abstract fun taskDao(): TaskDao
    abstract fun addProgrammerDao(): AddProgrammerDao

    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Project ADD COLUMN dt_prevista_fim INTEGER")
            }
        }
    }
}

