package com.example.projetoldii.repository

import com.example.projetoldii.AddProgrammerDao
import com.example.projetoldii.UserDao
import com.example.projetoldii.data.AddProgrammer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class ProgrammerItem(
    val id: Int,
    val userId: Int,
    val username: String,
    val nome: String,
    val nivel: String?,
    val departamento: String?,
    val addedAt: Long
)


class AddProgrammerRepository(
    private val userDao: UserDao,
    private val addProgrammerDao: AddProgrammerDao
) {
    /**
     * Adiciona um programador (por username) ao projeto.
     * Regras:
     * - user deve existir
     * - o owner (gestor) não pode se adicionar como programador
     * - não pode duplicar (mesmo user no mesmo projeto)
     * - pode estar em vários projetos (sem restrição global)
     */
    suspend fun addProgrammerByUsername(
        projectId: Int,
        ownerId: Int,
        username: String,
        nivel: String?,
        departamento: String?
    ): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByUsername(username.trim())
            ?: return@withContext Result.failure(IllegalArgumentException("Usuário não encontrado."))

        if (user.id_user == ownerId) {
            return@withContext Result.failure(IllegalArgumentException("O gestor não pode se adicionar como programador deste projeto."))
        }

        val already = addProgrammerDao.observeMembershipCount(projectId, user.id_user).first()
        if (already > 0) {
            return@withContext Result.failure(IllegalArgumentException("Este usuário já é programador neste projeto."))
        }

        val now = System.currentTimeMillis()
        addProgrammerDao.insert(
            AddProgrammer(
                id = 0,
                id_projeto = projectId,
                id_owner = ownerId,
                id_programador = user.id_user,
                nivel_experiencia = nivel,
                departamento = departamento,
                added_at = now
            )
        )
        Result.success(Unit)
    }
//    /** Observa a lista de programadores do projeto (com dados do usuário) */
//    fun observeProgrammers(projectId: Int): Flow<List<ProgrammerItem>> =
//        addProgrammerDao.observeProgrammersInProject(projectId).map { rows ->
//            rows.map { r ->
//                ProgrammerItem(
//                    id = r.id,
//                    userId = r.userId,
//                    username = r.username,
//                    nome = r.nome,
//                    nivel = r.nivel,
//                    departamento = r.departamento,
//                    addedAt = r.addedAt
//                )
//            }
//        }
}