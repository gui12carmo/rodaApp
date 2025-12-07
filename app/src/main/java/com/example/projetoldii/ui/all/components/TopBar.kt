package com.example.projetoldii.ui.all.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetoldii.ui.all.ProjetoLDIITheme

sealed interface TopBarVariant {
    data class Home(
        val onLogout: (() -> Unit)? = null,
    ) : TopBarVariant

    data class Project(
        val onBack: (() -> Unit)? = null,
        val onLogout: (() -> Unit)? = null,
    ) : TopBarVariant
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AppTopBar(
    title: String,
    variant: TopBarVariant,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },

        navigationIcon = {
            when (variant) {
                is TopBarVariant.Project -> {
                    variant.onBack?.let { onBack ->
                        IconButton(onClick = onBack) {
                            Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                is TopBarVariant.Home -> {  }
            }
        },

        actions = {
            val onLogout = when (variant) {
                is TopBarVariant.Home    -> variant.onLogout
                is TopBarVariant.Project -> variant.onLogout
            }
            onLogout?.let {
                IconButton(onClick = it) {
                    Icon(Icons.Outlined.ExitToApp, contentDescription = "Sair", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        },

        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Home TopBar")
@Composable
private fun PreviewHomeTopBar() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface (color = MaterialTheme.colorScheme.background) {
            AppTopBar(
                title = "Home",
                variant = TopBarVariant.Home(onLogout = {})
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Project TopBar")
@Composable
private fun PreviewProjectTopBar() {
    ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            AppTopBar(
                title = "Nome Projeto",
                variant = TopBarVariant.Project(onBack = {}, onLogout = {})
            )
        }
    }
}