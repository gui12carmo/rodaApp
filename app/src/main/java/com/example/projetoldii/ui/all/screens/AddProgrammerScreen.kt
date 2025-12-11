package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.viewmodels.AddProgrammerUi
import com.example.projetoldii.ui.all.viewmodels.Departamento
import com.example.projetoldii.ui.all.viewmodels.NivelExperiencia

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProgrammerScreen(
    state: AddProgrammerUi,
    onUsernameChange: (String) -> Unit,
    onNivelChange: (NivelExperiencia) -> Unit,
    onDepartamentoChange: (Departamento) -> Unit,
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
                title = "Adicionar Programador",
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
                    modifier = Modifier.weight(1f),
                    enabled = !state.isSaving
                ) { Text("CANCELAR") }

                Button(
                    onClick = onSave,
                    modifier = Modifier.weight(1f),
                    enabled = !state.isSaving
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
            // Username do usuário já cadastrado no sistema
            OutlinedTextField(
                value = state.username,
                onValueChange = onUsernameChange,
                label = { Text("Username do Programador*") },
                singleLine = true,
                isError = state.usernameError != null,
                supportingText = {
                    val msg = state.usernameError ?: state.formError
                    if (msg != null) Text(msg, color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Nível de experiência
            ExposedDropdownEnum(
                label = "Nível de experiência",
                value = state.nivel,
                all = NivelExperiencia.entries,
                onSelect = onNivelChange
            )

            // Departamento
            ExposedDropdownEnum(
                label = "Departamento",
                value = state.departamento,
                all = Departamento.entries,
                onSelect = onDepartamentoChange
            )
        }
    }

    // Dialog descartar alterações
    if (state.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = onDismissDiscard,
            title = { Text("Sair sem salvar?") },
            text = { Text("As alterações serão perdidas.") },
            confirmButton = { TextButton(onClick = onConfirmDiscard) { Text("CONTINUAR") } },
            dismissButton = { TextButton(onClick = onDismissDiscard) { Text("CANCELAR") } }
        )
    }

    // Dialog confirmar logout
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

/** Combo simples para enums usando ExposedDropdownMenuBox */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Enum<T>> ExposedDropdownEnum(
    label: String,
    value: T,
    all: List<T>,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = value.name,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            all.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
