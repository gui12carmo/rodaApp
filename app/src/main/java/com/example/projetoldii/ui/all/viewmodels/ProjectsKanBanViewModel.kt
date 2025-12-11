package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.Task
import com.example.projetoldii.domain.usecases.MoveTaskUseCase
import com.example.projetoldii.domain.usecases.ObserveBoardUseCase
import com.example.projetoldii.domain.usecases.ObserveProjectHeaderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TaskUi(
    val id: Int,
    val title: String,
    val assigneeName: String?,   // opcional: preencher quando tiver join
    val storyPoints: Int?,
    val statusTypeId: Int
)

data class ColumnUi(
    val typeId: Int,
    val title: String,
    val count: Int,
    val expanded: Boolean,
    val tasks: List<TaskUi>
)

data class ProjectKanBanUiState(
    val loading: Boolean = true,
    val error: String? = null,
    val projectTitle: String = "",
    val role: UserRole = UserRole.PROGRAMADOR,
    val userName: String = "",
    val columns: List<ColumnUi> = emptyList(),
    val showLogoutDialog: Boolean = false
)


class ProjectsKanBanViewModel(
    private val projectId: Int,
    private val observeBoard: ObserveBoardUseCase,
    private val moveTask: MoveTaskUseCase,
    private val currentUserId: Int,
    private val observeHeader: ObserveProjectHeaderUseCase,
    private val currentUserName: String
) : ViewModel() {

    // estado local de UI (expansão por coluna)
    private val expandedByTypeId = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    private val _ui = MutableStateFlow(ProjectKanBanUiState(userName = currentUserName))
    val ui: StateFlow<ProjectKanBanUiState> = _ui.asStateFlow()

    init {
        // Observa cabeçalho (título + role)
        val headerFlow = observeHeader(projectId, currentUserId)
            .map { header ->
                if (header == null) {
                    _ui.update { it.copy(loading = false, error = "Projeto não encontrado") }
                    return@map Pair("", UserRole.PROGRAMADOR)
                } else {
                    Pair(header.project.nome, header.role)
                }
            }

        // Observa board (tipos + tasks)
        val boardFlow = observeBoard(projectId)

        // Combina Header + Board + ExpandedMap
        viewModelScope.launch {
            combine(headerFlow, boardFlow, expandedByTypeId) { (title, role), board, expandedMap ->
                // Mapear BoardColumn → ColumnUi
                val columns = board.map { col ->
                    val expanded = expandedMap[col.type.id_tipoTarefa] ?: true
                    ColumnUi(
                        typeId = col.type.id_tipoTarefa,
                        title = col.type.nome,
                        count  = col.tasks.size,
                        expanded = expanded,
                        tasks = col.tasks.map { it.toUi() }
                    )
                }
                _ui.value.copy(
                    loading = false,
                    error = null,
                    projectTitle = title,
                    role = role,
                    columns = columns,
                    showLogoutDialog = _ui.value.showLogoutDialog
                )
            }.collect { combined ->
                _ui.value = combined
            }
        }
    }

    private fun Task.toUi() = TaskUi(
        id = this.id_tarefa,
        title = this.descricao ?: "(Sem descrição)",
        assigneeName = null, // preencha quando tiver join com User
        storyPoints = this.story_point,
        statusTypeId = this.id_tipoTarefa ?: -1
    )

    fun onToggleColumn(typeId: Int) {
        val curr = expandedByTypeId.value
        expandedByTypeId.value = curr.toMutableMap().apply {
            this[typeId] = !(this[typeId] ?: true)
        }
    }

    fun onMoveTask(task: TaskUi, toTypeId: Int) = viewModelScope.launch {
        // Você pode resolver Task real via repo se quiser evitar carregar tudo aqui.
        // Como já temos TaskUi, chame um método no repo que aceita taskId:
        // (Se precisar, adicione em TaskRepository um getById+moveTask(taskId, toTypeId))
        // Por ora, vamos mapear mínimo:
        val t = com.example.projetoldii.data.Task(
            id_tarefa = task.id,
            id_projeto = projectId,
            id_owner = currentUserId,  // não é usado no move
            id_programador = null,
            ordem_execucao = null,
            descricao = task.title,
            dt_prevista_inicio = null,
            dt_prevista_final = null,
            id_tipoTarefa = task.statusTypeId,
            story_point = task.storyPoints,
            dt_real_inicio = null,
            dt_real_fim = null,
            status = null,
            created_at = System.currentTimeMillis()
        )
        moveTask(t, toTypeId)
    }

    // Logout dialog
    fun requestLogout() { _ui.update { it.copy(showLogoutDialog = true) } }
    fun dismissLogout() { _ui.update { it.copy(showLogoutDialog = false) } }

    // helper opcional para formatação
    private fun format(millis: Long?): String =
        millis?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it)) } ?: "-"
}

class ProjectKanBanViewModelFactory(
    private val projectId: Int,
    private val currentUserId: Int,
    private val observeHeader: ObserveProjectHeaderUseCase,
    private val observeBoard: ObserveBoardUseCase,
    private val moveTask: MoveTaskUseCase,
    private val currentUserName: String
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ProjectsKanBanViewModel(projectId, observeBoard, moveTask, currentUserId, observeHeader, currentUserName) as T
    }
}
