package com.example.projetoldii.domain.usecases

import com.example.projetoldii.data.TaskType
import com.example.projetoldii.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveTaskTypesUseCase(private val repo: TaskRepository) {
    operator fun invoke(projectId: Int): Flow<List<TaskType>> =
        repo.observeTaskTypes(projectId)
}