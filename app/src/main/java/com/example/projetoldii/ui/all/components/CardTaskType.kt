package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.Light0
import com.example.projetoldii.ui.all.ProjetoLDIITheme

@Composable
fun CardTaskType(
    name: String,
    description: String,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier
) {
    Card(modifier = Modifier.padding(horizontal = 16.dp), shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
        containerColor = Light0)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Switch(checked = active, onCheckedChange = onActiveChange)
            }
            Row {
                Text("Descrição:", color = MaterialTheme.colorScheme.onSecondary)
                Spacer(Modifier.width(12.dp))
                Text(description, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Divider(color = MaterialTheme.colorScheme.outline)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(4.dp)
                ) { Text("EXCLUIR") }
            }
        }
    }
}

@Preview(showBackground = false, name = "CardTaskType")
@Composable
fun PreviewCardTaskType() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            CardTaskType(
                name = "FRONTEND",
                description = "Tarefas referentes a equipe de FrontEnd",
                active = false,
                onActiveChange = {},
                onDelete = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "CardTaskType - Lista")
@Composable
fun PreviewCardTaskType_List(){
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CardTaskType(
                    name = "FRONTEND",
                    description = "Tarefas referentes a",
                    active = false,
                    onActiveChange = {},
                    onDelete = {},
                    modifier = Modifier
                )
                CardTaskType(
                    name = "BackEnd",
                    description = "Tarefas referentes a",
                    active = true,
                    onActiveChange = {},
                    onDelete = {},
                    modifier = Modifier
                )
                CardTaskType(
                    name = "Marketing",
                    description = "Tarefas referentes a",
                    active = false,
                    onActiveChange = {},
                    onDelete = {},
                    modifier = Modifier
                )
            }
        }
    }
}