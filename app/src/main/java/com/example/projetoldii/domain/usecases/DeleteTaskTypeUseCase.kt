package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.TaskRepository

class DeleteTaskTypeUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(typeId: Int) = repo.deleteTaskType(typeId)
}