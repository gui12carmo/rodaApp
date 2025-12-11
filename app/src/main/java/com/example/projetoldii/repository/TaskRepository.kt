package com.example.projetoldii.repository

import com.example.projetoldii.TaskDao
import com.example.projetoldii.data.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun observeCompletedTasks(userId: Int): Flow<List<Task>> {
        return taskDao.getCompletedTasks(userId)
    }
}
