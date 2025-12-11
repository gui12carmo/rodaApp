package com.example.projetoldii.domain.usecases

import com.example.projetoldii.data.Task
import com.example.projetoldii.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveProgrammerCompletedTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(userId: Int): Flow<List<Task>> {
        return repository.observeCompletedTasks(userId)
    }
}
