@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.projetoldii.ui.all.screens

import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projetoldii.ui.all.components.AppTopBar
import com.example.projetoldii.ui.all.components.CardProject
import com.example.projetoldii.ui.all.components.TopBarVariant
import com.example.projetoldii.ui.all.viewmodels.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onOpenProject: (projectId: Int) -> Unit,
    onCreateProject: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Home",
                variant = TopBarVariant.Home(onLogout = onLogout)
            )
        }
    ) { padding ->
        when {
            uiState.loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.items.isEmpty() -> {
                EmptyProjects(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    onCreate = onCreateProject
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = uiState.items,
                        key = { it.id }
                    ) { item ->
                        CardProject(
                            title = item.title,
                            managerName = item.managerName,
                            startDate = item.startDate,
                            isManager = item.isManager,
                            onClick = { onOpenProject(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyProjects(
    modifier: Modifier = Modifier,
    onCreate: () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Não possui projetos ativos.\nCrie um projeto ou seja convidado para um existente.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onCreate,
                shape = MaterialTheme.shapes.large
            ) {
                Text("+ CRIAR PROJETO")
            }
        }
    }
}
