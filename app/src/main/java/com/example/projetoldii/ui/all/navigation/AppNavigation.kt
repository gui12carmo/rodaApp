package com.example.projetoldii.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetoldii.domain.usecases.MoveTaskUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.ui.all.routes.HomeRoute
import com.example.projetoldii.ui.all.routes.ProjectFormRoute
import com.example.projetoldii.ui.all.routes.ProjectKanBanRoute
import com.example.projetoldii.ui.all.screens.LoginScreen
import com.example.projetoldii.ui.all.screens.RegisterScreen
import com.example.projetoldii.ui.viewmodels.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("Home")
    object CreateProject : Screen("createProject")
    object ProjectKanBan : Screen("project/{id}") {
        fun routeOf(id: Int) = "project/$id"
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    taskRepo: TaskRepository,
    moveTaskUseCase: MoveTaskUseCase,    // 👈 novo parâmetro, bate com o MainActivity
) {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { _ ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeEntry(
                authViewModel = authViewModel,
                projectRepo = projectRepo,
                navController = navController
            )
        }

        composable(Screen.CreateProject.route) {
            CreateProjectEntry(
                authViewModel = authViewModel,
                projectRepo = projectRepo,
                navController = navController
            )
        }

        composable(Screen.ProjectKanBan.route) { backStackEntry ->
            ProjectKanBanEntry(
                authViewModel = authViewModel,
                projectRepo = projectRepo,
                taskRepo = taskRepo,
                navController = navController,
                projectId = backStackEntry.arguments
                    ?.getString("id")
                    ?.toIntOrNull()
                    ?: return@composable
                // se quiser, futuramente podemos passar também o moveTaskUseCase
                // moveTaskUseCase = moveTaskUseCase,
            )
        }
    }
}

@Composable
private fun HomeEntry(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    navController: NavHostController
) {
    val user = authViewModel.currentUser.value

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (user != null) {
        HomeRoute(
            projectRepository = projectRepo,
            currentUserId = user.id_user,
            onOpenProject = { projectId ->
                navController.navigate(Screen.ProjectKanBan.routeOf(projectId))
            },
            onCreateProject = {
                navController.navigate(Screen.CreateProject.route)
            },
            onLogout = {
                authViewModel.currentUser.value = null
            }
        )
    }
}

@Composable
private fun CreateProjectEntry(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    navController: NavHostController,
) {
    val user = authViewModel.currentUser.value

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    if (user == null) return

    ProjectFormRoute(
        projectRepository = projectRepo,
        currentUserId = user.id_user,
        onSavedGoBack = { navController.popBackStack() },
        onLogoutNav = {
            authViewModel.currentUser.value = null
        },
        onCancelNav = { navController.popBackStack() }
    )
}

@Composable
private fun ProjectKanBanEntry(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    taskRepo: TaskRepository,
    navController: NavHostController,
    projectId: Int,
    // se quiser usar no futuro:
    // moveTaskUseCase: MoveTaskUseCase,
) {
    val user = authViewModel.currentUser.value

    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    if (user == null) return

    ProjectKanBanRoute(
        projectRepository = projectRepo,
        currentUserId = user.id_user,
        currentUserName = user.nome,
        projectId = projectId,
        taskRepository = taskRepo,
        onBack = { navController.popBackStack() },
        onLogout = { authViewModel.currentUser.value = null },
        onOpenTaskDetail = { /* nav para detalhe quando existir */ },
        onCreateTask = { /* nav para criação de task */ },
        onCreateTaskType = { /* nav para criação de tipo */ },
        onAddProgrammer = { /* nav para adicionar programador */ }
    )
}