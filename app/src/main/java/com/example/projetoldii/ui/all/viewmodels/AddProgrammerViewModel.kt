package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.domain.usecases.AddProgrammerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class AddProgrammerUi(
    val username: String = "",
    val nivel: NivelExperiencia = NivelExperiencia.JUNIOR,
    val departamento: Departamento = Departamento.IT,

    val isSaving: Boolean = false,
    val isDirty: Boolean = false,

    val usernameError: String? = null,
    val formError: String? = null,

    val showDiscardDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)


class AddProgrammerViewModel(
    private val projectId: Int,
    private val ownerId: Int,
    private val addProgrammer: AddProgrammerUseCase
): ViewModel() {
    private val _ui = MutableStateFlow(AddProgrammerUi())
    val ui: StateFlow<AddProgrammerUi> = _ui.asStateFlow()

    // --- inputs
    fun onUsernameChange(v: String) {
        _ui.update { it.copy(username = v, usernameError = null, formError = null, isDirty = true) }
    }

    fun onNivelChange(v: NivelExperiencia) {
        _ui.update { it.copy(nivel = v, isDirty = true) }
    }

    fun onDepartamentoChange(v: Departamento) {
        _ui.update { it.copy(departamento = v, isDirty = true) }
    }

    // --- dialogs
    fun requestDiscard() { _ui.update { it.copy(showDiscardDialog = true) } }
    fun dismissDiscard() { _ui.update { it.copy(showDiscardDialog = false) } }

    fun requestLogout() { _ui.update { it.copy(showLogoutDialog = true) } }
    fun dismissLogout() { _ui.update { it.copy(showLogoutDialog = false) } }

    // --- validação simples
    private fun validate(): Boolean {
        var ok = true
        val s = _ui.value

        if (s.username.isBlank()) {
            _ui.update { it.copy(usernameError = "Username é obrigatório") }
            ok = false
        }

        return ok
    }

    // --- salvar
    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        if (!validate()) return@launch
        val s = _ui.value
        _ui.update { it.copy(isSaving = true, formError = null) }

        addProgrammer(
            projectId = projectId,
            ownerId = ownerId,
            username = s.username.trim(),
            nivel = s.nivel.name,   // armazenando como String
            departamento = s.departamento.name // idem
        ).onSuccess {
            _ui.value = AddProgrammerUi() // limpa
            onSaved()
        }.onFailure { e ->
            _ui.update { it.copy(isSaving = false, formError = e.message ?: "Erro ao adicionar programador") }
        }
    }
}

class AddProgrammerViewModelFactory(
    private val projectId: Int,
    private val ownerId: Int,
    private val addProgrammer: AddProgrammerUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return AddProgrammerViewModel(projectId, ownerId, addProgrammer) as T
    }
}