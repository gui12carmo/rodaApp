package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.TaskRepository

class ToggleTaskTypeActiveUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(typeId: Int, active: Boolean) =
        repo.toggleTaskTypeActive(typeId, active)
}