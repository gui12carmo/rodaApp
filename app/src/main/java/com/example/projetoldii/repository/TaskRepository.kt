package com.example.projetoldii.repository

import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.TaskDao
import com.example.projetoldii.TaskTypeDao
import com.example.projetoldii.data.Task
import com.example.projetoldii.data.TaskType
import com.example.projetoldii.domain.usecases.model.TaskState

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map


data class BoardColumnStatus(
    val state: TaskState,
    val tasks: List<Task>
)

class TaskRepository(
    private val taskDao: TaskDao,
    private val taskTypeDao: TaskTypeDao,
    private val addProgrammerDao: AddProgrammerDao
) {

    fun observeTaskTypes(projectId: Int): Flow<List<TaskType>> =
        taskTypeDao.observeByProject(projectId)

    suspend fun createTaskType(
        projectId: Int,
        nome: String,
        descricao: String?,
        ativo: Boolean
    ): Result<Long> {
        if (nome.isBlank()) return Result.failure(IllegalArgumentException("Nome é obrigatório."))
        if ((descricao?.length ?: 0) > 100) {
            return Result.failure(IllegalArgumentException("Descrição deve ter no máximo 100 caracteres."))
        }
        // (opcional) garantir nome único
        val exists = taskTypeDao.countByName(projectId, nome.trim())
        if (exists > 0) return Result.failure(IllegalArgumentException("Já existe um tipo com esse nome."))

        val id = taskTypeDao.insert(
            TaskType(
                id_tipoTarefa = 0,
                id_projeto = projectId,
                nome = nome.trim(),
                descricao = descricao?.trim(),
                status = ativo
            )
        )
        return Result.success(id)
    }

    suspend fun toggleTaskTypeActive(typeId: Int, active: Boolean): Result<Unit> {
        val current = taskTypeDao.getById(typeId)
            ?: return Result.failure(IllegalArgumentException("Tipo não encontrado."))
        taskTypeDao.update(current.copy(status = active))
        return Result.success(Unit)
    }

    suspend fun deleteTaskType(typeId: Int): Result<Unit> {
        val current = taskTypeDao.getById(typeId)
            ?: return Result.failure(IllegalArgumentException("Tipo não encontrado."))
        // FK em Task.id_tipoTarefa já está com SET_NULL, então é seguro remover
        taskTypeDao.delete(current)
        return Result.success(Unit)
    }


//    fun observeBoard(projectId: Int): Flow<List<BoardColumn>> {
//        val typesFlow = taskTypeDao.observeByProject(projectId)
//        val tasksFlow = taskDao.observeByProject(projectId)
//        return combine(typesFlow, tasksFlow) { types, tasks ->
//            types.map { t ->
//                BoardColumn(
//                    type = t,
//                    tasks = tasks.filter { it.id_tipoTarefa == t.id_tipoTarefa }
//                )
//            }
//        }
//    }

    fun observeBoardByStatus(projectId: Int): Flow<List<BoardColumnStatus>>
    = taskDao.observeByProject(projectId).map { all ->
        val toDo = all.filter { (it.status ?: "TO_DO") == "TO_DO" }
        val doing = all.filter { (it.status ?: "TO_DO") == "DOING"  }
        val done = all.filter { (it.status ?: "TO_DO") == "DONE" }
        listOf(
            BoardColumnStatus(TaskState.TODO, toDo),
            BoardColumnStatus(TaskState.DOING, doing),
            BoardColumnStatus(TaskState.DONE, done)
        )
    }

    suspend fun moveTaskToState(taskId: Int, to: TaskState) {
        taskDao.updateStatus(taskId, to.name)
    }

    /** Atualiza o tipo/status da task (mover entre colunas). */
    suspend fun moveTask(task: Task, toTypeId: Int) {
        val updated = task.copy(id_tipoTarefa = toTypeId)
        taskDao.update(updated)
    }
}