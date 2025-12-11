package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.TaskType
import com.example.projetoldii.domain.usecases.DeleteTaskTypeUseCase
import com.example.projetoldii.domain.usecases.ObserveTaskTypesUseCase
import com.example.projetoldii.domain.usecases.ToggleTaskTypeActiveUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class TaskTypeItemUi(
    val id: Int,
    val name: String,
    val description: String?,
    val active: Boolean
)

data class TaskTypeListUi(
    val loading: Boolean = true,
    val items: List<TaskTypeItemUi> = emptyList(),
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val deleteTargetId: Int? = null
)




class TaskTypeListViewModel(
    private val projectId: Int,
    private val observeTaskTypes: ObserveTaskTypesUseCase,
    private val toggleActive: ToggleTaskTypeActiveUseCase,
    private val deleteType: DeleteTaskTypeUseCase
): ViewModel() {

    private val _ui = MutableStateFlow(TaskTypeListUi())
    val ui: StateFlow<TaskTypeListUi> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            observeTaskTypes(projectId)
                .onStart { _ui.update { it.copy(loading = true, error = null) } }
                .map { list -> list.map { it.toUi() } }
                .catch { e -> _ui.update { it.copy(loading = false, error = e.message) } }
                .collect { items ->
                    _ui.update { it.copy(loading = false, items = items, error = null) }
                }
        }
    }
    private fun TaskType.toUi() = TaskTypeItemUi(
        id = id_tipoTarefa,
        name = nome,
        description = descricao,
        active = status
    )

    fun onToggleActive(id: Int, newActive: Boolean) = viewModelScope.launch {
        toggleActive(id, newActive)
    }

    fun requestDelete(id: Int) {
        _ui.update { it.copy(showDeleteDialog = true, deleteTargetId = id) }
    }

    fun dismissDelete() {
        _ui.update { it.copy(showDeleteDialog = false, deleteTargetId = null) }
    }

    fun confirmDelete() = viewModelScope.launch {
        val id = _ui.value.deleteTargetId ?: return@launch
        deleteType(id)
        _ui.update { it.copy(showDeleteDialog = false, deleteTargetId = null) }
    }
}

class TaskTypeListViewModelFactory(
    private val projectId: Int,
    private val observeTaskTypes: ObserveTaskTypesUseCase,
    private val toggleActive: ToggleTaskTypeActiveUseCase,
    private val deleteType: DeleteTaskTypeUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return TaskTypeListViewModel(projectId, observeTaskTypes, toggleActive, deleteType) as T
    }
}