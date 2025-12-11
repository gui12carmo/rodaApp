package com.example.projetoldii.domain.usecases

import com.example.projetoldii.data.Task
import com.example.projetoldii.domain.usecases.model.TaskState
import com.example.projetoldii.repository.TaskRepository

class MoveTaskUseCase(
    private val repo: TaskRepository
) {
    suspend operator fun invoke(taskId: Int, to: TaskState) =
        repo.moveTaskToState( taskId , to)
}