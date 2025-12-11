package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetoldii.domain.usecases.model.TaskState
import com.example.projetoldii.ui.all.components.*
import com.example.projetoldii.ui.all.routes.TaskTypeListRoute
import com.example.projetoldii.ui.all.viewmodels.ProjectKanBanUiState
import com.example.projetoldii.ui.all.viewmodels.TaskUi
import com.example.projetoldii.ui.all.viewmodels.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectKanBanScreen(
    state: ProjectKanBanUiState,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onConfirmLogout: () -> Unit,
    onDismissLogout: () -> Unit,
    onToggleColumn: (TaskState) -> Unit,
    onMoveTask: (TaskUi, TaskState) -> Unit,
    onOpenTaskDetail: (taskId: Int) -> Unit,
    onCreateTask: () -> Unit,
    onCreateTaskType: () -> Unit,
    onAddProgrammer: () -> Unit,
    typesTab: @Composable () -> Unit = {},
    reportersTab: @Composable () -> Unit = {}
) {
    val ProjectRole = remember(state.role) { state.role }

    var expanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(ProjectNav.LISTA) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = state.projectTitle.ifBlank { "Projeto" },
                variant = TopBarVariant.Project(
                    onBack = onBack,
                    onLogout = onLogout
                )
            )
        },
        floatingActionButton = {
            if (ProjectRole == UserRole.GESTOR) {
                SpeedDialFab(
                    items = listOf(
                        SpeedDialItem("Criar tarefa", Icons.Outlined.Add, onCreateTask),
                        SpeedDialItem("Criar tipo de tarefa", Icons.Outlined.Add, onCreateTaskType),
                        SpeedDialItem("Adicionar programador", Icons.Outlined.Add, onAddProgrammer),
                    ),
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                )
            }
        },
        bottomBar = {
            ProjectNavBar(
                role = ProjectRole,          // seu componente espera UserRole
                selected = selectedTab,
                onSelect = { selectedTab = it }
            )
        }
    ) { padding ->

        if (state.loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            BannerUser(
                userName = state.userName,   // sem aspas
                role = ProjectRole
            )

            when (selectedTab) {
                ProjectNav.LISTA -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.columns, key = { it.state }) { col ->
                            Column(Modifier.fillMaxWidth()) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${col.title} (${col.count})",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    IconButton(onClick = { onToggleColumn(col.state) }) {
                                        Icon(
                                            if (col.expanded) Icons.Outlined.ArrowDropDown else Icons.Outlined.ArrowRight,
                                            contentDescription = null
                                        )
                                    }
                                }

                                if (col.expanded) {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (col.tasks.isEmpty()) {
                                            Text(
                                                text = "Sem tarefas",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        } else {
                                            col.tasks.forEachIndexed { index, t ->
                                                val cardRole: CardTaskRole =
                                                    when (ProjectRole) {
                                                        UserRole.PROGRAMADOR -> CardTaskRole.Prog(
                                                            onChangeStatus = {
                                                                // por enquanto move para o MESMO estado da coluna
                                                                // TODO (depois você abre um picker para escolher o novo estado)
                                                                onMoveTask(t, col.state)
                                                            }
                                                        )
                                                        UserRole.GESTOR -> CardTaskRole.Manager(
                                                            onChangeStatus = {
                                                                onMoveTask(t, col.state)
                                                            },
                                                            onMoveUp = { /* TODO mover p/ cima */ },
                                                            onMoveDown = { /* TODO mover p/ baixo */ }
                                                        )
                                                    }

                                                CardTask(
                                                    number = index + 1,
                                                    title = t.title,
                                                    assignee = t.assigneeName ?: "-",
                                                    type = "t.typeName ?: -",  // sem aspas fixas
                                                    onClick = { onOpenTaskDetail(t.id) },
                                                    role = cardRole,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ProjectNav.TIPOS -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        typesTab()
                    }
                }

                ProjectNav.RELATORIOS -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Relatórios (em breve)")
                        //reportersTab()
                    }
                }
            }
        }
    }

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
