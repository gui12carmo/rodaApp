package com.example.projetoldii.repository

import com.example.projetoldii.ui.all.viewmodels.UserRole

import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.ProjectDao
import com.example.projetoldii.data.Project
import com.example.projetoldii.ui.all.models.ProjectWhithRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val addProgrammerDao: AddProgrammerDao
) {

    fun observeProjectWithRole(projectId: Int, userId: Int): Flow<ProjectWhithRole?> {
        val projectFlow = projectDao.observeById(projectId)
        val isMemberFlow = addProgrammerDao
            .observeMembershipCount(projectId, userId)
            .map { it > 0}

        return combine(projectFlow, isMemberFlow) { project, isMember ->
            if (project == null) {
                null
            } else {
                val role = when {
                    project.id_owner == userId -> UserRole.GESTOR
                    isMember                   -> UserRole.PROGRAMADOR
                    else                       -> UserRole.PROGRAMADOR
                }
                ProjectWhithRole(project, role)
            }
        }
    }

    /**
     * Cria um novo projeto para o owner informado.
     * Regras:
     *  - nome é obrigatório (não blank)
     *  - descrição opcional (limitada a 100 chars – ajuste se quiser)
     *  - dtPrevistaFim opcional; se informado, pode ser hoje ou depois
     */
    suspend fun createProject(
        ownerId: Int,
        nome: String,
        descricao: String?,
        dtPrevistaFim: Long?
    ): Result<Long> = withContext(Dispatchers.IO) {
        // validações simples
        if (nome.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Nome do projeto é obrigatório."))
        }
        if ((descricao?.length ?: 0) > 100) {
            return@withContext Result.failure(IllegalArgumentException("Descrição deve ter no máximo 100 caracteres."))
        }

        val now = System.currentTimeMillis()
        val project = Project(
            nome = nome.trim(),
            descricao = descricao?.trim(),
            id_owner = ownerId,
            created_at = now,
            dt_prevista_fim = dtPrevistaFim
        )

        val id = projectDao.insert(project)
        Result.success(id) // retorna o rowId (ID do projeto recém-criado)
    }



    /**
     * Observa, em tempo real, todos os projetos do usuário:
     *  - como OWNER (id_owner = userId)
     *  - como MEMBER (vinculado em AddProgrammer)
     */

    fun observeProjectsForUser(userId: Int): Flow<List<ProjectWhithRole>> {
        // 1) Projetos onde ele é gestor
        val ownerFlow: Flow<List<ProjectWhithRole>> =
            projectDao.observeByOwner(userId).map { projects ->
                projects.map { ProjectWhithRole(it, UserRole.GESTOR) }
            }

        // 2) IDs de projetos onde ele é programador
        val memberIdsFlow = addProgrammerDao.observeByProgrammer(userId)
            .map { list -> list.map { it.id_projeto }.distinct() }

        // 3) Projetos correspondentes a esses IDs (reativo)
        val memberProjectsFlow: Flow<List<ProjectWhithRole>> =
            memberIdsFlow.flatMapLatest { ids ->
                if (ids.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    projectDao.observeByIds(ids).map { projects ->
                        projects.map { ProjectWhithRole(it, UserRole.PROGRAMADOR) }
                    }
                }
            }

        // 4) Combina Gestor + Programador e remove duplicados por id_projeto
        return combine(ownerFlow, memberProjectsFlow) { ownerList, memberList ->
            (ownerList + memberList).distinctBy { it.project.id_projeto }
        }
    }
}
