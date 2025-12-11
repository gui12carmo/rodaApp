package com.example.projetoldii.ui.all.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.AddProgrammerUseCase
import com.example.projetoldii.repository.AddProgrammerRepository
import com.example.projetoldii.ui.all.screens.AddProgrammerScreen
import com.example.projetoldii.ui.all.viewmodels.AddProgrammerViewModel
import com.example.projetoldii.ui.all.viewmodels.AddProgrammerViewModelFactory

@Composable
fun AddProgrammerRoute(
    projectId: Int,
    ownerId: Int,
    addProgrammerRepository: AddProgrammerRepository,
    onSavedGoBack: () -> Unit,
    onCancelNav: () -> Unit,
    onLogoutNav: () -> Unit
) {
    val vm: AddProgrammerViewModel = viewModel(
        factory = AddProgrammerViewModelFactory(
            projectId = projectId,
            ownerId = ownerId,
            addProgrammer = AddProgrammerUseCase(addProgrammerRepository)
        )
    )

    val state by vm.ui.collectAsState()

    AddProgrammerScreen(
        state = state,
        onUsernameChange = vm::onUsernameChange,
        onNivelChange = vm::onNivelChange,
        onDepartamentoChange = vm::onDepartamentoChange,
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
