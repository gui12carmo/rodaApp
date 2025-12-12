package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.all.components.BannerUser
import com.example.projetoldii.ui.all.components.ProjectNav
import com.example.projetoldii.ui.all.components.ProjectNavBar
import com.example.projetoldii.ui.all.viewmodels.ManagerReportItem
import com.example.projetoldii.ui.all.viewmodels.ManagerReportViewModel
import com.example.projetoldii.ui.all.viewmodels.UserRole   // ✅ usamos UserRole

// ---------- TELA REAL (usa ViewModel e dados do banco) ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerReportScreen(
    viewModel: ManagerReportViewModel,
    projectName: String,
    managerName: String,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
    onExportCsv: (String) -> Unit   // recebe o CSV pronto
) {
    val items by viewModel.items.collectAsState()

    ManagerReportContent(
        projectName = projectName,
        managerName = managerName,
        items = items,
        onBack = onBack,
        onNavigateToList = onNavigateToList,
        onExportCsv = {
            val csv = buildManagerReportCsv(items)
            onExportCsv(csv)
        }
    )
}

// ---------- CONTEÚDO PURO (Preview / testes) ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManagerReportContent(
    projectName: String,
    managerName: String,
    items: List<ManagerReportItem>,
    onBack: () -> Unit,
    onNavigateToList: () -> Unit,
    onExportCsv: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = projectName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowLeft,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    // 👉 ÍCONE NO CANTO SUPERIOR DIREITO – EXPORTAR CSV
                    IconButton(onClick = onExportCsv) {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = "Exportar CSV"
                        )
                    }
                }
            )
        },
        bottomBar = {
            ProjectNavBar(
                role = UserRole.GESTOR,   // ✅ gestor
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
            // Banner com nome do gestor
            BannerUser(
                userName = managerName,
                role = UserRole.GESTOR      // ✅ aqui era ProjectRole.Gestor
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Relatório do Projeto",
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
                    ManagerReportCard(item)
                }
            }
        }
    }
}

// ---------- CARD DO GESTOR (com nome do programador) ----------

@Composable
private fun ManagerReportCard(item: ManagerReportItem) {
    val deliveredColor = if (item.deliveredDays <= item.expectedDays) {
        MaterialTheme.colorScheme.primary   // no prazo / antes do prazo
    } else {
        MaterialTheme.colorScheme.error     // atrasado
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                        fontWeight = FontWeight.Bold,
                        color = deliveredColor
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Programador: ${item.programmerName}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// ---------- FUNÇÃO PARA GERAR O CSV ----------

/**
 * Formato:
 * titulo;programador;dias_previstos;dias_entregues
 */
private fun buildManagerReportCsv(items: List<ManagerReportItem>): String {
    val header = "titulo;programador;dias_previstos;dias_entregues\n"
    val lines = items.joinToString(separator = "\n") { item ->
        "${item.title};${item.programmerName};${item.expectedDays};${item.deliveredDays}"
    }
    return header + lines
}

// ---------- PREVIEW ----------

@Preview(showBackground = true, name = "Relatório Gestor")
@Composable
private fun ManagerReportPreview() {
    val fakeItems = listOf(
        ManagerReportItem(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 5
        ),
        ManagerReportItem(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 4
        ),
        ManagerReportItem(
            title = "Nome da Task",
            programmerName = "Thales Pires",
            expectedDays = 4,
            deliveredDays = 3
        )
    )

    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ManagerReportContent(
            projectName = "Nome Projeto",
            managerName = "Gabriella Rezende",
            items = fakeItems,
            onBack = {},
            onNavigateToList = {},
            onExportCsv = {}
        )
    }
}
