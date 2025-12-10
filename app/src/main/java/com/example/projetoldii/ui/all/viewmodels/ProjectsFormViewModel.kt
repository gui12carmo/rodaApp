package com.example.projetoldii.ui.all.viewmodels

import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.domain.usecases.CreateProjectUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ProjectFormUi(
    val name: String = "",
    val description: String = "",
    val endDateMillis: Long? = null,     // previsão de fim
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val isDirty: Boolean = false,        // algo foi alterado?
    val showDiscardDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)


class ProjectsFormViewModel(
    private val createProject: CreateProjectUseCase,
    private val ownerId: Int
): ViewModel() {
    private val _ui = MutableStateFlow(ProjectFormUi())
    val ui: StateFlow<ProjectFormUi> = _ui.asStateFlow()

    fun onNameChange(v: String)  {
        _ui.update{ it.copy (name = v, nameError = null, isDirty = true)}
    }

    fun onDescriptionChange(v: String)  { _ui.update {it.copy(description = v, isDirty = true) } }

    fun onEndDateChange(millis: Long?)  {_ui.update{ it.copy(endDateMillis = millis, isDirty = true) }}


    //Dilog descartar
    fun requestLogout() { _ui.update { it.copy(showLogoutDialog = true) } }
    fun dismissLogout() { _ui.update { it.copy( showLogoutDialog= false) } }

    // Dilog Logout
    fun requestDiscard() { _ui.update { it.copy( showDiscardDialog= true) } }
    fun dismissDiscard() { _ui.update { it.copy(showDiscardDialog = false) } }

    private fun validate(): Boolean {
        var ok = true
        if (_ui.value.name.isBlank()) {
            _ui.update { it.copy(nameError = "Nome do projeto é obrigatório") }
            ok = false
        }
        return ok
    }

    /**
     * Tenta salvar e chama [onSaved] quando concluir com sucesso.
     */
    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        val s = _ui.value
        if (s.name.isBlank()) {
            _ui.update { it.copy(nameError = "Nome do projeto é obrigatório") }
            return@launch
        }
        _ui.update { it.copy(isSaving = true) }

        createProject(
            nome = s.name ,
            descricao = s.description.ifBlank { null },
            dtPrevistaFim = s.endDateMillis,
            ownerId = ownerId,
        ) .onSuccess {
            _ui.value = ProjectFormUi()
            onSaved()
        } .onFailure { e ->
            _ui.update { it.copy(isSaving = false, nameError = e.message ?: "Erro ao salvar") }
        }
    }
}

class ProjectFormViewModelFactory(
    private val createProject: CreateProjectUseCase,
    private val ownerId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProjectsFormViewModel(createProject, ownerId) as T
    }
}