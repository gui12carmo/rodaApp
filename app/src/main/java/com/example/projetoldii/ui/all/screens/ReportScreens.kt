package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.BannerUser
import com.example.projetoldii.ui.all.components.ManagerReportCard
import com.example.projetoldii.ui.all.components.ProgrammerReportCard
import com.example.projetoldii.ui.all.components.ProjectNav
import com.example.projetoldii.ui.all.components.ProjectNavBar
import com.example.projetoldii.ui.all.components.TaskReportUi
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.components.buildManagerReportCsv
import com.example.projetoldii.ui.all.components.buildProgrammerReportCsv
import com.example.projetoldii.ui.all.viewmodels.UserRole

// ---------- API pública: telas separadas ----------

@Composable
fun ProgrammerReportScreen(
    projectName: String,
    programmerName: String,
    tasks: List<TaskReportUi>,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
    onExportCsv: (String) -> Unit,   // recebe a string CSV pronta
) {
    ReportScreenBase(
        role = UserRole.PROGRAMADOR,
        projectName = projectName,
        userName = programmerName,
        tasks = tasks,
        onBack = onBack,
        onNavigateToList = onNavigateToList,
        onExportCsv = { onExportCsv(buildProgrammerReportCsv(tasks)) },
    )
}

@Composable
fun ManagerReportScreen(
    projectName: String,
    managerName: String,
    tasks: List<TaskReportUi>,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
    onExportCsv: (String) -> Unit,
) {
    ReportScreenBase(
        role = UserRole.GESTOR,
        projectName = projectName,
        userName = managerName,
        tasks = tasks,
        onBack = onBack,
        onNavigateToList = onNavigateToList,
        onExportCsv = { onExportCsv(buildManagerReportCsv(tasks)) },
    )
}

// ---------- Implementação compartilhada ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReportScreenBase(
    role: UserRole,
    projectName: String,
    userName: String,
    tasks: List<TaskReportUi>,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
    onExportCsv: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = projectName,
                variant = TopBarVariant.Project(onBack = onBack)
            )
        },
        bottomBar = {
            ProjectNavBar(
                role = role,
                selected = ProjectNav.RELATORIOS,
                onSelect = { nav ->
                    when (nav) {
                        ProjectNav.LISTA -> onNavigateToList()
                        ProjectNav.RELATORIOS -> Unit
                        else -> Unit
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BannerUser(
                userName = userName,
                role = role
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Minhas Concluídas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "TEMPO GASTO REAL (DIAS) EM CADA TAREFA",
                style = MaterialTheme.typography.bodySmall
            )

            // Botão de EXPORTAR CSV (simples, você pode mover pro topo depois)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onExportCsv) {
                    Text("Exportar CSV")
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                when (role) {
                    UserRole.PROGRAMADOR -> {
                        items(tasks) { item ->
                            ProgrammerReportCard(item = item)
                        }
                    }
                    UserRole.GESTOR -> {
                        items(tasks) { item ->
                            ManagerReportCard(item = item)
                        }
                    }
                }
            }
        }
    }
}

// ---------- PREVIEWS ----------

@Preview(showBackground = true, name = "Relatório - Programador")
@Composable
private fun ProgrammerReportPreview() {
    val fakeTasks = listOf(
        TaskReportUi(
            title = "Nome da Task",
            programmerName = null,
            expectedDays = 4,
            deliveredDays = 5
        ),
        TaskReportUi(
            title = "Nome da Task",
            programmerName = null,
            expectedDays = 4,
            deliveredDays = 3
        )
    )

    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ProgrammerReportScreen(
            projectName = "Nome Projeto",
            programmerName = "Guilherme Carmo",
            tasks = fakeTasks,
            onBack = {},
            onNavigateToList = {},
            onExportCsv = { csv -> println(csv) } // no preview só imprime no log
        )
    }
}

@Preview(showBackground = true, name = "Relatório - Gestor")
@Composable
private fun ManagerReportPreview() {
    val fakeTasks = listOf(
        TaskReportUi(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 5
        ),
        TaskReportUi(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 4
        ),
        TaskReportUi(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 3
        )
    )

    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ManagerReportScreen(
            projectName = "Nome Projeto",
            managerName = "Gabriella Rezende",
            tasks = fakeTasks,
            onBack = {},
            onNavigateToList = {},
            onExportCsv = { csv -> println(csv) }
        )
    }
}