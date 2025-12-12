package com.example.projetoldii.domain.usecases

import com.example.projetoldii.data.Task
import com.example.projetoldii.repository.TaskRepository

class MoveTaskUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(task: Task, toTypeId: Int) =
        repo.moveTask( task , toTypeId)
}



