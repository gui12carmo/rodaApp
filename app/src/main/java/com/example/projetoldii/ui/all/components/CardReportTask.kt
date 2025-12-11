package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.projetoldii.data.Task

// Modelo usado por todas as telas de relatório
data class TaskReportUi(
    val title: String,
    val programmerName: String?, // null para programador, preenchido para gestor
    val expectedDays: Int,
    val deliveredDays: Int
)

@Composable
private fun deliveredColor(expected: Int, delivered: Int) =
    when {
        delivered > expected -> MaterialTheme.colorScheme.error       // atrasado (vermelho)
        delivered < expected -> MaterialTheme.colorScheme.primary     // adiantado (azul/verde)
        else -> MaterialTheme.colorScheme.onSurfaceVariant            // no prazo (cinza)
    }

/**
 * CARD para o PROGRAMADOR
 * (sem linha "Programador:", igual ao layout do dev)
 */
@Composable
fun ProgrammerReportCard(
    item: TaskReportUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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

            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.End
            ) {
                Text(
                    text = "ENTREGUE EM:",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "${item.deliveredDays} DIAS",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = deliveredColor(item.expectedDays, item.deliveredDays)
                )
            }
        }
    }
}

/**
 * CARD para o GESTOR
 * (com linha "Programador: Fulano", igual ao mock)
 */
@Composable
fun ManagerReportCard(
    item: TaskReportUi,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                        color = deliveredColor(item.expectedDays, item.deliveredDays)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            item.programmerName?.let { name ->
                Text(
                    text = "Programador: $name",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

