package com.example.projetoldii.repository

import com.example.projetoldii.ui.all.viewmodels.UserRole

import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.ProjectDao
import com.example.projetoldii.ui.all.models.ProjectWhithRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

class ProjectRepository(
    private val projectDao: ProjectDao,
    private val addProgrammerDao: AddProgrammerDao
) {

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
