package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.ProjectRepository

class CreateProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke (
        ownerId: Int,
        nome: String,
        descricao: String?,
        dtPrevistaFim: Long?
    ) = repository.createProject(ownerId, nome, descricao, dtPrevistaFim)
}