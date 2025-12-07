package com.example.projetoldii.ui.all.components

import android.R.attr.contentDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import kotlin.math.exp

data class SpeedDialItem(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SpeedDialFab(
    items: List<SpeedDialItem>,
    expanded: Boolean,
    onExpandedChange:   (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier, contentAlignment = Alignment.BottomEnd) {
        Column(
            modifier = Modifier
                .padding(end = 8.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + slideInVertically { it / 2 } + expandVertically(),
                    exit = fadeOut() + slideOutVertically { it / 2 } + shrinkVertically ()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            shape = MaterialTheme.shapes.large,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            )
                        }

                        SmallFloatingActionButton(
                            onClick = {
                                item.onClick()
                                onExpandedChange(false)
                            },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            shape = CircleShape
                        ) {
                            Icon(item.icon, contentDescription = item.label)
                        }
                    }
                }
            }
        }

        val rotation by animateFloatAsState(
            targetValue = if (expanded) 45f else 0f,
            label = "fab-rotation"
        )
        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = if (expanded) "Fechar" else "Abrir",
                modifier = Modifier.graphicsLayer { rotationZ = rotation }
            )
        }
    }
}

@Preview(showBackground = true, name = "SpeedDial – Fechado")
@Composable
fun PreviewSpeedDial_Collapsed() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Box(Modifier.fillMaxSize()) {
            var expanded by remember { mutableStateOf(false) }

            // conteúdo fake
            Text(
                "Tela de exemplo",
                modifier = Modifier.padding(24.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            SpeedDialFab(
                items = listOf(
                    SpeedDialItem("Criar Tarefa", Icons.Outlined.Add) {},
                    SpeedDialItem("Criar Tipo de Tarefa", Icons.Outlined.Add) {},
                    SpeedDialItem("Adicionar Programador", Icons.Outlined.Add) {},
                ),
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

