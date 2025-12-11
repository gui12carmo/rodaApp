package com.example.projetoldii.ui.all.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.CreateTaskTypeUseCase
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.ui.all.screens.TaskTypeFormScreen
import com.example.projetoldii.ui.all.viewmodels.TaskTypeFormViewModel
import com.example.projetoldii.ui.all.viewmodels.TaskTypeFormViewModelFactory

@Composable
fun TaskTypeFormRoute(
    projectId: Int,
    taskRepository: TaskRepository,
    onSavedGoBack: () -> Unit,
    onCancelNav: () -> Unit,
    onLogoutNav: () -> Unit
) {
    val vm: TaskTypeFormViewModel = viewModel(
        factory = TaskTypeFormViewModelFactory(
            projectId = projectId,
            createTaskType = CreateTaskTypeUseCase(taskRepository)
        )
    )
    val state by vm.ui.collectAsState()

    TaskTypeFormScreen(
        state = state,
        onNameChange = vm::onNameChange,
        onDescriptionChange = vm::onDescriptionChange,
        onActiveChange = vm::onActiveChange,
        onSave = { vm.save(onSavedGoBack) },
        onCancel = {
            if (state.isDirty) vm.requestDiscard() else onCancelNav()
        },
        onConfirmDiscard = { vm.dismissDiscard(); onCancelNav() },
        onDismissDiscard = vm::dismissDiscard,
        onLogout = vm::requestLogout,
        onConfirmLogout = { vm.dismissLogout(); onLogoutNav() },
        onDismissLogout = vm::dismissLogout
    )
}
