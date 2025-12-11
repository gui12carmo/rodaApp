package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.BoardColumnStatus
import com.example.projetoldii.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveBoardUseCase(
    private val repo: TaskRepository
) {
    operator fun invoke(projectId: Int): Flow<List<BoardColumnStatus>> =
        repo.observeBoardByStatus(projectId)
}