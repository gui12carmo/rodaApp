package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.Light0
import com.example.projetoldii.ui.all.Primary12
import com.example.projetoldii.ui.all.ProjetoLDIITheme
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
    Card(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
        containerColor = Light0
    )) {
        Column(Modifier.padding(16.dp)) {
            //Titulo
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp),verticalAlignment = Alignment.CenterVertically ) {
                Text("#$number",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Text("$title",
                    style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onTertiaryContainer)
            }

            Spacer(Modifier.height(16.dp))

            //metadados
            InfoRow("Responsável:", assignee)
            InfoRow("Tipo:", type)

            Spacer(Modifier.height(16.dp))


            //Ações por responsabilidade
            when (role) {
                is CardTaskRole.Viewer -> Unit
                is CardTaskRole.Prog -> {
                    Column() {
                        Divider(color = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(16.dp))
                        OutlinedButton(onClick = role.onChangeStatus, shape = RoundedCornerShape(4.dp)) { Text("MUDAR STATUS") }
                    }
                }
                is CardTaskRole.Manager -> {
                    Column() {
                        Divider(color = MaterialTheme.colorScheme.outline)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            OutlinedButton(onClick = role.onChangeStatus, shape = RoundedCornerShape(4.dp)) { Text("MUDAR STATUS") }
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
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, color = MaterialTheme.colorScheme.onSecondary)
        Text(value, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Preview(showBackground = false, name = "CardTask - Viewer")
@Composable
fun PreviewCardTask_Viewer(){
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface() {
            CardTask(
                number = 12,
                title = "Teste",
                assignee = "gabbi",
                type = "Bug",
                role = CardTaskRole.Viewer,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = false, name = "CardTask - Programador")
@Composable
fun PreviewCardTask_Prog(){
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface() {
            CardTask(
                number = 34,
                title = "Teste",
                assignee = "gabbi",
                type = "Bug",
                role = CardTaskRole.Prog(
                    onChangeStatus = {}
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = false, name = "CardTask - Gestor")
@Composable
fun PreviewCardTask_Manager(){
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface() {
            CardTask(
                number = 12,
                title = "Teste",
                assignee = "gabbi",
                type = "Bug",
                role = CardTaskRole.Manager(
                    onChangeStatus = {},
                    onMoveDown = {},
                    onMoveUp = {}
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "CardTask - Lista")
@Composable
fun PreviewCardTask_List(){
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(Modifier.padding(16.dp)) {
                CardTask(
                    number = 12,
                    title = "Nome da Task",
                    assignee = "gabbi",
                    type = "Bug",
                    role = CardTaskRole.Viewer
                )
                CardTask(
                    number = 34,
                    title = "Nome da Task",
                    assignee = "gabbi",
                    type = "Bug",
                    role = CardTaskRole.Prog(
                        onChangeStatus = {}
                    ),
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
                CardTask(
                    number = 12,
                    title = "Nome da Task",
                    assignee = "gabbi",
                    type = "Bug",
                    role = CardTaskRole.Manager(
                        onChangeStatus = {},
                        onMoveDown = {},
                        onMoveUp = {}
                    ),
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
            }
        }
    }
}