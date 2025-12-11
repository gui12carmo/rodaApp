package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.ui.all.models.ProjectWhithRole
import kotlinx.coroutines.flow.Flow

class ObserveProjectHeaderUseCase(
    private val repo: ProjectRepository
) {
    operator fun invoke(projectId: Int, userId: Int): Flow<ProjectWhithRole?> =
        repo.observeProjectWithRole(projectId, userId)
}
