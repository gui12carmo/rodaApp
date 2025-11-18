package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme

@Composable
fun CardProject(
    title: String,
    managerName: String,
    startDate: String,
    isManager: Boolean,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface)
                InfoRow("Gestor:", managerName)
                InfoRow("Data Início:", startDate)
            }
            if (isManager) {
                Icon(Icons.Filled.Person, contentDescription = "Gestor", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(label, color = MaterialTheme.colorScheme.outline)
        Text(value, color = MaterialTheme.colorScheme.tertiary)
    }
}

@Preview(showBackground = true, name = "CardProject - Gestor")
@Composable
fun PreviewCardProject_Manager() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface {
            CardProject(
                title = "Nome do Projeto",
                managerName = "Gabriella Rezende",
                startDate = "21/06/2025",
                isManager = true,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "CardProject - Programador")
@Composable
fun PreviewCardProject_Prog() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface {
            CardProject(
                title = "Nome do Projeto",
                managerName = "Gabriella Rezende",
                startDate = "21/06/2025",
                isManager = false,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "CardProject - Lista")
@Composable
fun PreviewCardProject_List() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                CardProject(
                    title = "Nome do Projeto",
                    managerName = "Gabriella Rezende",
                    startDate = "21/06/2025",
                    isManager = false,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                CardProject(
                    title = "Nome do Projeto",
                    managerName = "Gabriella Rezende",
                    startDate = "21/06/2025",
                    isManager = true,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                CardProject(
                    title = "Nome do Projeto",
                    managerName = "Gabriella Rezende",
                    startDate = "21/06/2025",
                    isManager = true,
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}