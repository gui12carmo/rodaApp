package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.viewmodels.TaskTypeFormUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTypeFormScreen(
    state: TaskTypeFormUi,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    onConfirmDiscard: () -> Unit,
    onDismissDiscard: () -> Unit,
    onLogout: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDismissLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Novo Tipo de Tarefa",
                variant = TopBarVariant.Project(
                    onBack = onCancel,
                    onLogout = onLogout
                )
            )
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) { Text("CANCELAR") }

                Button(
                    onClick = onSave,
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) { Text(if (state.isSaving) "SALVANDO..." else "SALVAR") }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Nome*") },
                singleLine = true,
                isError = state.nameError != null,
                supportingText = {
                    state.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                label = { Text("Descrição (até 100 caracteres)") },
                minLines = 3,
                supportingText = { Text("${state.description.length}/100") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 96.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Ativo", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = state.active, onCheckedChange = onActiveChange)
            }
        }
    }

    // descartar alterações
    if (state.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = onDismissDiscard,
            title = { Text("Sair sem salvar?") },
            text = { Text("As alterações serão perdidas.") },
            confirmButton = { TextButton(onClick = onConfirmDiscard) { Text("SAIR") } },
            dismissButton = { TextButton(onClick = onDismissDiscard) { Text("CANCELAR") } }
        )
    }

    // logout
    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogout,
            title = { Text("Deseja sair?") },
            text = { Text("Tem certeza que quer desligar da sua conta?") },
            confirmButton = { TextButton(onClick = onConfirmLogout) { Text("SIM") } },
            dismissButton = { TextButton(onClick = onDismissLogout) { Text("NÃO") } }
        )
    }
}
