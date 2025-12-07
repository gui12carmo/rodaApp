package com.example.projetoldii.ui.all.components
import com.example.projetoldii.ui.all.viewmodels.TaskState
import com.example.projetoldii.ui.all.viewmodels.UserRole
import com.example.projetoldii.ui.all.viewmodels.Departamento
import com.example.projetoldii.ui.all.viewmodels.NivelExperiencia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme



@Composable
private fun Pill(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// Linha "rótulo : valor"
@Composable
private fun LabeledValue(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            label,
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.widthIn(min = 120.dp).weight(1f)
        )
        Text(
            value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
fun TaskDetailCard(
    // Cabeçalho
    order: Int,
    title: String,
    state: TaskState,

    // Metadados
    createdAt: String,
    projectName: String,
    ownerName: String,
    programmerName: String,
    experience: NivelExperiencia,
    department: Departamento,

    // Conteúdo
    description: String,
    storyPoints: Int,
    taskTypeName: String,

    // Datas
    plannedStart: String?,
    plannedEnd: String?,
    realStart: String?,
    realEnd: String?,

    // Papel
    role: UserRole,

    // Ações
    onChangeStatus: () -> Unit,
    onEdit: () -> Unit = {},

    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#$order",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Normal
                    )
                }
                Text(
                    text = when (state) {
                        TaskState.TODO -> "TO-DO"
                        TaskState.DOING -> "DOING"
                        TaskState.DONE -> "DONE"
                    },
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "CRIADO EM: $createdAt",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )

            Divider(color = MaterialTheme.colorScheme.outline)

            // Bloco 1
            LabeledValue("Projeto:", projectName)
            LabeledValue("Owner:", ownerName)
            LabeledValue("Programador:", programmerName)
            LabeledValue(
                "Nível de Experiência:",
                if (experience == NivelExperiencia.JUNIOR) "Júnior" else "Sénior"
            )
            LabeledValue(
                "Departamento:",
                when (department) {
                    Departamento.IT -> "TI"
                    Departamento.MARKETING -> "Marketing"
                    Departamento.ADMINISTRACAO -> "Administração"
                }
            )

            Divider(color = MaterialTheme.colorScheme.outline)

            // Bloco 2
            LabeledValue("Descrição", description)
            LabeledValue("Story Point:", storyPoints.toString())
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Tipo da Tarefa:",
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.widthIn(min = 120.dp).weight(1f)
                )
                Pill(taskTypeName)
                Spacer(Modifier.weight(1f))
            }

            Divider(color = MaterialTheme.colorScheme.outline)

            // Bloco 3 (datas)
            LabeledValue("Previsão de\nInício:", plannedStart ?: "--")
            LabeledValue("Previsão de\nFim:", plannedEnd ?: "--")
            LabeledValue("Início Real:", realStart ?: "--")
            LabeledValue("Final Real:", realEnd ?: "--")

            // Ações (variam por papel)
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (role == UserRole.GESTOR) {
                    OutlinedButton(onClick = onEdit,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)) {
                        Text("EDITAR")
                    }
                    Button(
                        onClick = onChangeStatus,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("MUDAR STATUS") }
                } else {
                    Button(
                        onClick = onChangeStatus,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("MUDAR STATUS") }
                }
            }
        }
    }
}

@Composable
private fun fake(role: UserRole) = TaskDetailCard(
    order = 2,
    title = "Nome da Task",
    state = TaskState.TODO,
    createdAt = "20/06/2025 23:16",
    projectName = "Nome Projeto",
    ownerName = "Gabriella Rezende",
    programmerName = "Guilherme Carmo",
    experience = NivelExperiencia.JUNIOR,
    department = Departamento.IT,
    description = "Descrição da Tarefa",
    storyPoints = 2,
    taskTypeName = "Tipo da tarefa",
    plannedStart = "21/06/2025",
    plannedEnd = "22/06/2025",
    realStart = null,
    realEnd = null,
    role = role,
    onChangeStatus = {},
    onEdit = {}
)

@Preview(showBackground = true, name = "Programador")
@Composable fun Preview_TaskDetail_Programador() {
    ProjetoLDIITheme (darkTheme = false, dynamicColor = false) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            fake(UserRole.PROGRAMADOR)
        }
    }
}


@Preview(showBackground = true, name = "Gestor")
@Composable fun Preview_TaskDetail_Gestor() {
    ProjetoLDIITheme (darkTheme = false, dynamicColor = false) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            fake(UserRole.GESTOR)
        }
    }
}
