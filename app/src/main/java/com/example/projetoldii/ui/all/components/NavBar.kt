package com.example.projetoldii.ui.all.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.all.viewmodels.UserRole

enum class ProjectNav { LISTA, TIPOS, RELATORIOS }

@Composable
fun ProjectNavBar(
    role: UserRole,
    selected: ProjectNav,
    onSelect: (ProjectNav) -> Unit,
) {
    val items: List<Triple<ProjectNav, String, ImageVector>> = when (role) {
        UserRole.PROGRAMADOR -> listOf(
            Triple(ProjectNav.LISTA, "LISTA", Icons.Outlined.List),
            Triple(ProjectNav.RELATORIOS, "RELATÓRIOS", Icons.Outlined.Share),
        )
        UserRole.GESTOR -> listOf(
            Triple(ProjectNav.LISTA, "LISTA", Icons.Outlined.ViewKanban),
            Triple(ProjectNav.TIPOS, "TIPOS DE TAREFAS", Icons.Outlined.Discount),
            Triple(ProjectNav.RELATORIOS, "RELATÓRIOS", Icons.Outlined.Analytics),
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { (dest, label, icon) ->
            val isSelected = dest == selected
            val tint = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant

            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(dest) },
                icon = {
                    Box(
                        modifier = Modifier.size(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = tint
                        )
                    }
                },
                label = { Text(label, color = tint) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Programador")
@Composable
private fun PreviewProjectNavBarProg() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ProjectNavBar(
            role = UserRole.PROGRAMADOR,
            selected = ProjectNav.LISTA,
            onSelect = {}
        )
    }
}

@Preview(showBackground = true, name = "Gestor")
@Composable
private fun PreviewProjectNavBarGes() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        ProjectNavBar(
            role = UserRole.GESTOR,
            selected = ProjectNav.TIPOS,
            onSelect = {}
        )
    }
}