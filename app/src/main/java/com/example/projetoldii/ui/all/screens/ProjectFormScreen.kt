package com.example.projetoldii.ui.all.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.viewmodels.ProjectFormUi
import com.example.projetoldii.ui.all.viewmodels.ProjectsFormViewModel
import androidx.compose.material3.AlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFormScreen(
    state: ProjectFormUi,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onEndDateChange: (Long?) -> Unit,
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
                title = "Home",
                variant = TopBarVariant.Home(onLogout = onLogout)
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
            Text("Dados do Projeto", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Nome do Projeto*") },
                singleLine = true,
                isError = state.nameError != null,
                supportingText = { state.nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = {
                    if (it.length <= 100) onDescriptionChange(it)
                },
                label = { Text("Descrição") },
                minLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                supportingText = {
                    Text("${state.description.length}/100")
                }
            )

            // Previsão de fim
            DatePickerField(
                label = "Previsão de Fim do Projeto",
                valueMillis = state.endDateMillis,
                onValueChange = onEndDateChange
            )
        }
    }

    // ---- Dialog: sair do cadastro sem salvar
    if (state.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = onDismissDiscard,
            title = { Text("Sair do cadastro?") },
            text = { Text("Ao continuar as informações incluídas serão desconsideradas.") },
            confirmButton = {
                TextButton(onClick = onConfirmDiscard) { Text("CONTINUAR") }
            },
            dismissButton = {
                TextButton(onClick = onDismissDiscard) { Text("CANCELAR") }
            }
        )
    }

    // ---- Dialog: confirmar logout
    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = onDismissLogout,
            title = { Text("Deseja sair?") },
            text = { Text("Tem certeza que quer desligar da sua conta?") },
            confirmButton = {
                TextButton(onClick = onConfirmLogout) { Text("SIM") }
            },
            dismissButton = {
                TextButton(onClick = onDismissLogout) { Text("NÃO") }
            }
        )
    }
}

/** Campo com DatePicker do Material 3 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    label: String,
    valueMillis: Long?,
    onValueChange: (Long?) -> Unit
) {
    var showPicker = androidx.compose.runtime.remember { false }
    OutlinedTextField(
        value = valueMillis?.let { java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date(it)) } ?: "",
        onValueChange = { /* somente via picker */ },
        label = { Text(label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = { /* setado no onDateChange abaixo */ showPicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
            }
        ) {
            val state = rememberDatePickerState(initialSelectedDateMillis = valueMillis)
            DatePicker(state = state)
            // quando fechar com OK:
            LaunchedEffect(state.selectedDateMillis) {
                // você pode mover para o confirmButton se preferir
                onValueChange(state.selectedDateMillis)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_DiscardDialog() {
    ProjectFormScreen(
        state = ProjectFormUi(
            name = "Teste",
            description = "x",
            showDiscardDialog = true    // força o diálogo abrir
        ),
        onNameChange = {},
        onDescriptionChange = {},
        onEndDateChange = {},
        onSave = {},
        onCancel = {},
        onConfirmDiscard = {},
        onDismissDiscard = {},
        onLogout = {},
        onConfirmLogout = {},
        onDismissLogout = {}
    )
}

