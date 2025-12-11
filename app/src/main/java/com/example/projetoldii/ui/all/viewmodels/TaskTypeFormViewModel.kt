package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.domain.usecases.CreateTaskTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class TaskTypeFormUi(
    val name: String = "",
    val description: String = "",
    val active: Boolean = true,
    val nameError: String? = null,
    val isDirty: Boolean = false,
    val isSaving: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)

class TaskTypeFormViewModel(
    private val projectId: Int,
    private val createTaskType: CreateTaskTypeUseCase
): ViewModel() {
    private val _ui = MutableStateFlow(TaskTypeFormUi())
    val ui: StateFlow<TaskTypeFormUi> = _ui.asStateFlow()

    fun onNameChange(v: String) = _ui.update {
        it.copy(name = v, nameError = null, isDirty = true)
    }

    fun onDescriptionChange(v: String) = _ui.update {
        it.copy(description = if (v.length <= 100) v else it.description, isDirty = true)
    }

    fun onActiveChange(v: Boolean) = _ui.update { it.copy(active = v, isDirty = true) }

    // dialogs
    fun requestDiscard() = _ui.update { it.copy(showDiscardDialog = true) }
    fun dismissDiscard() = _ui.update { it.copy(showDiscardDialog = false) }
    fun requestLogout() = _ui.update { it.copy(showLogoutDialog = true) }
    fun dismissLogout() = _ui.update { it.copy(showLogoutDialog = false) }

    private fun validate(): Boolean {
        var ok = true
        if (_ui.value.name.isBlank()) {
            _ui.update { it.copy(nameError = "Nome é obrigatório") }
            ok = false
        }
        return ok
    }

    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        if (!validate()) return@launch
        val s = _ui.value
        _ui.update { it.copy(isSaving = true) }

        createTaskType(
            projectId = projectId,
            nome = s.name.trim(),
            descricao = s.description.ifBlank { null },
            ativo = s.active
        ).onSuccess {
            _ui.value = TaskTypeFormUi()  // limpa estado
            onSaved()
        }.onFailure { e ->
            _ui.update { it.copy(isSaving = false, nameError = e.message ?: "Erro ao salvar") }
        }
    }
}

class TaskTypeFormViewModelFactory(
    private val projectId: Int,
    private val createTaskType: CreateTaskTypeUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return TaskTypeFormViewModel(projectId, createTaskType) as T
    }
}