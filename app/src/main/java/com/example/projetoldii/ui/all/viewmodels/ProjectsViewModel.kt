package com.example.projetoldii.ui.all.viewmodels

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.Project
import com.example.projetoldii.domain.usecases.ObserveUserProjectUseCase
import com.example.projetoldii.ui.all.models.ProjectWhithRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale


data class ProjectListItem(
    val id: Int,
    val title: String,
    val managerName: String,
    val startDate: String,
    val isManager: Boolean
)

data class HomeUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val items: List<ProjectListItem> = emptyList()
)

class ProjectsViewModel(
    private val observeProjects: ObserveUserProjectUseCase,
    private val userId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeProjects(userId)
                .onStart { _uiState.update { it.copy(loading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(loading = false, error = e.message) } }
                .collect { list ->

                    // list: List<ProjectWithRole>
                    val items: List<ProjectListItem> = list.map { pr ->
                        val p = pr.project
                        ProjectListItem(
                            id = p.id_projeto,
                            title = p.nome,
                            managerName = "Gestor #${p.id_owner}",        // placeholder por enquanto
                            startDate = formatDateOrDash(p.created_at),   // helper abaixo
                            isManager = pr.role == UserRole.GESTOR
                        )
                    }

                    _uiState.update { it.copy(loading = false, items = items, error = null) }
                }
        }
    }

    private fun formatDateOrDash(millis: Long?): String {
        if (millis == null || millis == 0L) return "--/--/----"
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(millis)
    }


    class ProjectsViewModelFactory(
        private val observeProjects: ObserveUserProjectUseCase,
        private val userId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProjectsViewModel(observeProjects, userId) as T

        }
    }
}