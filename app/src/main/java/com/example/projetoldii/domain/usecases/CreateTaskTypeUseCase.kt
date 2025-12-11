package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.TaskRepository

class CreateTaskTypeUseCase(private val repo: TaskRepository) {
    suspend operator fun invoke(
        projectId: Int,
        nome: String,
        descricao: String?,
        ativo: Boolean
    ) = repo.createTaskType(projectId, nome, descricao, ativo)
}