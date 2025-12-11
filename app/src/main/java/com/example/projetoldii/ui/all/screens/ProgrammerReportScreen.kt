package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.BannerUser
import com.example.projetoldii.ui.all.components.ProjectNav
import com.example.projetoldii.ui.all.components.ProjectNavBar
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.viewmodels.UserRole
import com.example.projetoldii.viewmodels.ProgrammerReportItem
import com.example.projetoldii.viewmodels.ProgrammerReportViewModel

// ---------- TELA REAL (usando ViewModel) ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgrammerReportScreen(
    viewModel: ProgrammerReportViewModel,
    projectName: String,
    programmerName: String,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
) {
    val items by viewModel.items.collectAsState()

    ProgrammerReportContent(
        projectName = projectName,
        programmerName = programmerName,
        items = items,
        onBack = onBack,
        onNavigateToList = onNavigateToList
    )
}

// ---------- CONTEÚDO PURO (sem ViewModel – usado no Preview) ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgrammerReportContent(
    projectName: String,
    programmerName: String,
    items: List<ProgrammerReportItem>,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
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
                role = UserRole.PROGRAMADOR,
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
                userName = programmerName,
                role = UserRole.PROGRAMADOR
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

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    ProgrammerReportCard(item)
                }
            }
        }
    }
}

// ---------- CARD SIMPLES PARA A TELA DO PROGRAMADOR ----------

@Composable
private fun ProgrammerReportCard(item: ProgrammerReportItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "TEMPO PREVISTO: ${item.expectedDays} DIAS",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    text = "ENTREGUE EM:",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${item.deliveredDays} DIAS",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ---------- PREVIEW ----------

@Preview(showBackground = true, name = "Relatório Programador")
@Composable
private fun ProgrammerReportPreview() {
    val fakeItems = listOf(
        ProgrammerReportItem(
            title = "Nome da Task",
            expectedDays = 4,
            deliveredDays = 5
        ),
        ProgrammerReportItem(
            title = "Nome da Task",
            expectedDays = 4,
            deliveredDays = 5
        ),
        ProgrammerReportItem(
            title = "Nome da Task",
            expectedDays = 4,
            deliveredDays = 5
        )
    )

    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ProgrammerReportContent(
            projectName = "Nome Projeto",
            programmerName = "Guilherme Carmo",
            items = fakeItems,
            onBack = {},
            onNavigateToList = {}
        )
    }
}