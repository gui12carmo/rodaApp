package com.example.projetoldii.ui.all.components

import android.R
import android.graphics.Color
import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.projetoldii.ui.all.Light200
import com.example.projetoldii.ui.all.Primary12
import com.example.projetoldii.ui.all.PrimaryColor

enum class UserRole {PROGRAMADOR, GESTOR}
enum class ProjectNav {LISTA, TIPOS, RELATORIOS}

/**private data class NavItemCfg(
    val dest: ProjectNav,
    val label: String,
    val icon: ImageVector
)

private fun itemsFor(role: UserRole) = when (role) {
    UserRole.PROGRAMADOR -> listOf(
        NavItemCfg(ProjectNav.LISTA, "LISTA", Icons.Outlined.List),
        NavItemCfg(ProjectNav.RELATORIOS, "RELATÓRIOS", Icons.Outlined.Share),
    )
    UserRole.GESTOR -> listOf(
        NavItemCfg(ProjectNav.LISTA, "LISTA", Icons.Outlined.List),
        NavItemCfg(ProjectNav.TIPOS, "TIPOS DE TAREFAS", Icons.Outlined.Menu),
        NavItemCfg(ProjectNav.RELATORIOS, "RELATÓRIOS", Icons.Outlined.Share),
        )
}**/



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
            Triple(ProjectNav.LISTA, "LISTA", Icons.Outlined.List),
            Triple(ProjectNav.TIPOS, "TIPOS DE TAREFAS", Icons.Outlined.Menu),
            Triple(ProjectNav.RELATORIOS, "RELATÓRIOS", Icons.Outlined.Share),
        )
    }

    NavigationBar {
        items.forEach { triple ->
            val dest = triple.first
            val label = triple.second
            val icon = triple.third

            NavigationBarItem(
                selected = dest == selected,
                onClick = { onSelect(dest) },
                icon = { Icon(icon, contentDescription = label)},
                label = { Text(label)}
            )
    }

    }

}

@Preview(showBackground = true, name = "Programador")
@Composable
private fun PreviewProjectNavBar() {
    ProjectNavBar(
        role = UserRole.PROGRAMADOR,
        selected = ProjectNav.LISTA,
        onSelect = {}
    )
}