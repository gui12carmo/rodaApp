package com.example.projetoldii.ui.all.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.ObserveUserProjectUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.ui.all.screens.HomeScreen
import com.example.projetoldii.ui.all.viewmodels.HomeUiState
import com.example.projetoldii.ui.all.viewmodels.ProjectsViewModel
import com.example.projetoldii.ui.all.viewmodels.ProjectsViewModelFactory

@Composable
fun HomeRoute(
    projectRepository: ProjectRepository,
    currentUserId: Int,
    onOpenProject: (Int) -> Unit,
    onCreateProject: () -> Unit,
    onLogout: () -> Unit
) {
    val vm: ProjectsViewModel = viewModel(
        factory = ProjectsViewModelFactory(
            observeProjects = ObserveUserProjectUseCase(projectRepository),
            userId = currentUserId
        )
    )

    val HomeUiState = vm.uiState.collectAsState()


    HomeScreen(
        uiState = HomeUiState.value,
        onOpenProject = onOpenProject,
        onCreateProject = onCreateProject,
        onLogoutClick = vm::requestLogout,
        onConfirmLogout = {
            vm.dismissLogout()
            onLogout()
        },
        onDismissLogout = vm::dismissLogout,
        onCreate = onCreateProject
    )
}