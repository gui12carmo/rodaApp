package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.ProjectRepository

class ObserveUserProjectUseCase(
    private val repository: ProjectRepository
) {
    operator fun invoke(userId: Int) = repository.observeProjectsForUser(userId)
}
