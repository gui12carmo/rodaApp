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

private data class NavItemCfg(
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
}



@Composable
fun RoleNavBar(
    role: UserRole,
    selected: ProjectNav,
    onSelect: (ProjectNav) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = itemsFor(role)
    Surface(color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
    items.forEach { cfg ->
        val isSelected = cfg.dest == selected
        val color: androidx.compose.ui.graphics.Color =
            if (isSelected) PrimaryColor
            else Light200

        Column(
            modifier = modifier
                .widthIn(min = 88.dp)
                .clickable {onSelect(cfg.dest) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(cfg.icon, contentDescription = cfg.label, tint = color, modifier = modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(
                text = cfg.label,
                color = color,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
        }
    }
        }
    }
}

@Preview(showBackground = true, name = "Programador")
@Composable
private fun PreviewNavProgramador() {
    RoleNavBar(
        role = UserRole.PROGRAMADOR,
        selected = ProjectNav.LISTA,
        onSelect = {}
    )
}

@Preview(showBackground = true, name = "Gestor")
@Composable
private fun PreviewNavGestor() {
    RoleNavBar(
        role = UserRole.GESTOR,
        selected = ProjectNav.TIPOS,
        onSelect = {}
    )
}