package com.example.projetoldii.ui.all.routes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.DeleteTaskTypeUseCase
import com.example.projetoldii.domain.usecases.ObserveTaskTypesUseCase
import com.example.projetoldii.domain.usecases.ToggleTaskTypeActiveUseCase
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.ui.all.components.CardTaskType
import com.example.projetoldii.ui.all.viewmodels.TaskTypeListUi
import com.example.projetoldii.ui.all.viewmodels.TaskTypeListViewModel
import com.example.projetoldii.ui.all.viewmodels.TaskTypeListViewModelFactory

@Composable
fun TaskTypeListRoute(
    projectId: Int,
    taskRepository: TaskRepository,
    onCreateType: () -> Unit
) {
    val vm: TaskTypeListViewModel = viewModel(
        factory = TaskTypeListViewModelFactory(
            projectId = projectId,
            observeTaskTypes = ObserveTaskTypesUseCase(taskRepository),
            toggleActive = ToggleTaskTypeActiveUseCase(taskRepository),
            deleteType = DeleteTaskTypeUseCase(taskRepository)
        )
    )

    val state by vm.ui.collectAsState()

    TaskTypeListContent(
        state = state,
        onCreateType = onCreateType,
        onToggleActive = { id, newActive -> vm.onToggleActive(id, newActive)},
        onDeleteRequest = { id -> vm.requestDelete(id) },
        onDismissDelete = vm::dismissDelete,
        onConfirmDelete = vm::confirmDelete
    )
}

/** Conteúdo visual da aba (sem virar “Screen” separada) */
@Composable
private fun TaskTypeListContent(
    state: TaskTypeListUi,
    onCreateType: () -> Unit,
    onToggleActive: (id: Int, newActive: Boolean) -> Unit,
    onDeleteRequest: (id: Int) -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit

) {
    // Dialog de confirmação de exclusão
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = onDismissDelete,
            title = { Text("Excluir tipo de tarefa?") },
            text = { Text("Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = onConfirmDelete) { Text("EXCLUIR") }
            },
            dismissButton = {
                TextButton(onClick = onDismissDelete) { Text("CANCELAR") }
            }
        )
    }

    when {
        state.loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error ?: "Erro inesperado")
            }
        }

        state.items.isEmpty() -> {
            // vazio + CTA criar tipo
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nenhum tipo de tarefa.")
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.items, key = { it.id }) { tt ->
                    // Ajuste os parâmetros conforme a assinatura do seu CardTaskType
                    CardTaskType(
                        name = tt.name,
                        description = tt.description ?: "",
                        active = tt.active,
                        onActiveChange = { newActive -> onToggleActive(tt.id, newActive) },
                        onDelete = { onDeleteRequest(tt.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
