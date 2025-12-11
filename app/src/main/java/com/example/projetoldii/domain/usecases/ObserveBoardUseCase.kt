package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.BoardColumn
import com.example.projetoldii.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class ObserveBoardUseCase(
    private val repo: TaskRepository
) {
    operator fun invoke(projectId: Int): Flow<List<BoardColumn>> =
        repo.observeBoard(projectId)
}