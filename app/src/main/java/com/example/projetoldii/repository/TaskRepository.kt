package com.example.projetoldii.repository

import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.TaskDao
import com.example.projetoldii.TaskTypeDao
import com.example.projetoldii.data.Task
import com.example.projetoldii.data.TaskType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class BoardColumn(
    val type: TaskType,
    val tasks: List<Task>
)

class TaskRepository(
    private val taskDao: TaskDao,
    private val taskTypeDao: TaskTypeDao,
    private val addProgrammerDao: AddProgrammerDao
) {

    /** Tarefas concluídas de um programador (relatório do programador). */
    fun observeCompletedTasks(userId: Int): Flow<List<Task>> {
        return taskDao.getCompletedTasks(userId)
    }

    /** Board/Kanban do projeto: colunas por tipo de tarefa. */
    fun observeBoard(projectId: Int): Flow<List<BoardColumn>> {
        val typesFlow = taskTypeDao.observeByProject(projectId)
        val tasksFlow = taskDao.observeByProject(projectId)

        return combine(typesFlow, tasksFlow) { types, tasks ->
            types.map { t ->
                BoardColumn(
                    type = t,
                    tasks = tasks.filter { it.id_tipoTarefa == t.id_tipoTarefa }
                )
            }
        }
    }

    /** Atualiza o tipo/status da task (mover entre colunas). */
    suspend fun moveTask(task: Task, toTypeId: Int) {
        val updated = task.copy(id_tipoTarefa = toTypeId)
        taskDao.update(updated)
    }
}