package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.AddProgrammerRepository

class AddProgrammerUseCase(
    private val repo: AddProgrammerRepository
) {
    suspend operator fun invoke(
        projectId: Int,
        ownerId: Int,
        username: String,
        nivel: String?,
        departamento: String?
    ) = repo.addProgrammerByUsername(projectId, ownerId, username, nivel, departamento)
}