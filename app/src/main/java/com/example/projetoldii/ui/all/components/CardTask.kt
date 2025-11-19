package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.sql.ClientInfoStatus

sealed interface CardTaskRole{
    data object Viewer : CardTaskRole
    data class Prog(val onChangeStatus: () -> Unit) : CardTaskRole
    data class Manager(
        val onChangeStatus: () -> Unit,
        val onMoveUp: () -> Unit,
        val onMoveDown: () -> Unit
    ) : CardTaskRole
}

@Composable
fun CardTask(
    number: Int,
    title: String,
    assignee: String,
    type: String,
    onClick: () -> Unit = {},
    role: CardTaskRole,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large) {
        Column(Modifier.padding(16.dp)) {
            //Titulo
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("#$number  $title",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            Spacer(Modifier.height(12.dp))

            //metadados
            InfoRow("Responsável:", assignee)
            InfoRow("Tipo:", type)

            Spacer(Modifier.height(16.dp))

            //Ações por responsabilidade
            when (role) {
                is CardTaskRole.Viewer -> Unit
                is CardTaskRole.Prog -> {
                    OutlinedButton(onClick = role.onChangeStatus) { Text("MUDAR STATUS") }
                }
                is CardTaskRole.Manager -> {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(onClick = role.onChangeStatus) { Text("MUDAR STATUS") }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(onClick = role.onMoveUp) { Icon(Icons.Outlined.KeyboardArrowUp, null) }
                            IconButton(onClick = role.onMoveDown) { Icon(Icons.Outlined.KeyboardArrowDown, null) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value)
    }
}