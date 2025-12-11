package com.example.projetoldii.ui.all.routes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.domain.usecases.MoveTaskUseCase
import com.example.projetoldii.domain.usecases.ObserveBoardUseCase
import com.example.projetoldii.domain.usecases.ObserveProjectHeaderUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.ui.all.screens.ProjectKanBanScreen
import com.example.projetoldii.ui.all.viewmodels.ProjectKanBanViewModelFactory
import com.example.projetoldii.ui.all.viewmodels.ProjectsKanBanViewModel
import com.example.projetoldii.ui.viewmodels.AuthViewModel

@Composable
fun ProjectKanBanRoute(
    projectId: Int,
    currentUserId: Int,
    currentUserName: String,
    projectRepository: ProjectRepository,
    taskRepository: TaskRepository,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onOpenTaskDetail: (taskId: Int) -> Unit,
    onCreateTask: (projectId: Int) -> Unit,
    onCreateTaskType: (projectId: Int) -> Unit,
    onAddProgrammer: (projectId: Int) -> Unit
) {
    val vm: ProjectsKanBanViewModel = viewModel(
        factory = ProjectKanBanViewModelFactory(
            projectId = projectId,
            currentUserId = currentUserId,
            observeHeader = ObserveProjectHeaderUseCase(projectRepository),
            observeBoard  = ObserveBoardUseCase(taskRepository),
            moveTask      = MoveTaskUseCase(taskRepository),
            currentUserName = currentUserName
        )
    )

    val state by vm.ui.collectAsState()

    ProjectKanBanScreen(
        state = state,
        typesTab = {
            TaskTypeListRoute(
                projectId = projectId,
                taskRepository = taskRepository,
                onCreateType = { // TODO: navegue para o formulário de novo tipo usando o projectId do escopo
                    // ex.: navController.navigate(Screen.CreateTaskType.routeOf(projectId))
                },
            )
        },
        reportersTab = { androidx.compose.material3.Text("Relatórios (em breve)") },
        onBack = onBack,
        onLogout = vm::requestLogout,
        onConfirmLogout = {
            vm.dismissLogout()
            onLogout()
        },
        onDismissLogout = vm::dismissLogout,
        onToggleColumn = vm::onToggleColumn,
        onMoveTask = vm::onMoveTask,
        onOpenTaskDetail = onOpenTaskDetail,
        onCreateTask = { onCreateTask(projectId) },
        onCreateTaskType = { onCreateTaskType(projectId) },
        onAddProgrammer = { onAddProgrammer(projectId) }
    )
}