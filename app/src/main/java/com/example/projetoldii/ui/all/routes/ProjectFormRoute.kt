package com.example.projetoldii.ui.all.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.CreateProjectUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.ui.all.screens.ProjectFormScreen
import com.example.projetoldii.ui.all.viewmodels.ProjectsFormViewModel
import com.example.projetoldii.ui.all.viewmodels.ProjectFormViewModelFactory

@Composable
fun ProjectFormRoute(
    projectRepository: ProjectRepository,
    currentUserId: Int,
    onSavedGoBack: () -> Unit,
    onLogoutNav: () -> Unit,
    onCancelNav: () -> Unit
) {
    val vm: ProjectsFormViewModel = viewModel(
        factory = ProjectFormViewModelFactory(
            createProject = CreateProjectUseCase(projectRepository),
            ownerId = currentUserId
        )
    )

    val state by vm.ui.collectAsState()

    ProjectFormScreen(
        state = state,
        onNameChange = vm::onNameChange,
        onDescriptionChange = vm::onDescriptionChange,
        onEndDateChange = vm::onEndDateChange,
        onSave = { vm.save(onSavedGoBack) },
        onCancel = {
            if (state.isDirty) vm.requestDiscard() else onCancelNav()
        },
        onConfirmDiscard = {
            vm.dismissDiscard()
            onCancelNav()
        },
        onDismissDiscard = vm::dismissDiscard,
        onLogout = vm::requestLogout,
        onConfirmLogout = {
            vm.dismissLogout()
            onLogoutNav()
        },
        onDismissLogout = vm::dismissLogout
    )
}
