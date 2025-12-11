package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.Task
import com.example.projetoldii.domain.usecases.MoveTaskUseCase
import com.example.projetoldii.domain.usecases.ObserveBoardUseCase
import com.example.projetoldii.domain.usecases.ObserveProjectHeaderUseCase
import com.example.projetoldii.domain.usecases.model.TaskState
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
    val storyPoints: Int?
)

data class ColumnUi(
    val state: TaskState,
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
    private val expandedByState = MutableStateFlow<Map<TaskState, Boolean>>(emptyMap())

    private val _ui = MutableStateFlow(ProjectKanBanUiState(userName = currentUserName))
    val ui: StateFlow<ProjectKanBanUiState> = _ui.asStateFlow()

    init {
        // Observa cabeçalho (título + role)
        val headerFlow = observeHeader(projectId, currentUserId)
            .map { header ->
                if (header == null) {
                    _ui.update { it.copy(loading = false, error = "Projeto não encontrado") }
                    "" to UserRole.PROGRAMADOR
                } else {
                    header.project.nome to header.role
                }
            }

        // Observa board (tipos + tasks)
        val boardFlow = observeBoard(projectId)


    viewModelScope.launch {
        combine(headerFlow, boardFlow, expandedByState) {
                (title, role), board, expandedMap ->
            val columns = board.map { col ->
                val expanded = expandedMap[col.state] ?: true
                ColumnUi(
                    state = col.state,
                    title = when (col.state) {
                        TaskState.TODO -> "ToDo"
                        TaskState.DOING -> "Doing"
                        TaskState.DONE -> "Done"
                    },
                    count = col.tasks.size,
                    expanded = expanded,
                    tasks = col.tasks.map { it.toUi() }
                )
            }
            ProjectKanBanUiState(
                loading = false,
                error = null,
                projectTitle = title,
                role = role,
                userName = currentUserName,
                columns = columns,
                showLogoutDialog = _ui.value.showLogoutDialog
            )
        }.collect { combined -> _ui.value = combined }
    }
    }

    private fun Task.toUi() = TaskUi(
        id = this.id_tarefa,
        title = this.descricao ?: "(Sem descrição)",
        assigneeName = null, // preencha quando tiver join com User
        storyPoints = this.story_point
    )

    fun onToggleColumn(state: TaskState) {
        val curr = expandedByState.value
        expandedByState.value = curr.toMutableMap().apply {
            this[state] = !(this[state] ?: true)
        }
    }

    fun onMoveTask(task: TaskUi, toState: TaskState) = viewModelScope.launch {
        moveTask(task.id, toState)
    }

    // Logout dialog
    fun requestLogout() { _ui.update { it.copy(showLogoutDialog = true) } }
    fun dismissLogout() { _ui.update { it.copy(showLogoutDialog = false) } }

    // helper opcional para formatação
    private fun format(millis: Long?): String =
        millis?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it)) } ?: "-"
}

private fun Task.toUi() = TaskUi(
    id = id_tarefa,
    title = descricao ?: "(Sem descrição)",
    assigneeName = null,
    storyPoints = story_point
)

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
