package com.example.projetoldii.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetoldii.DatabaseProvider
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.ui.all.screens.LoginScreen
import com.example.projetoldii.ui.all.screens.RegisterScreen
import com.example.projetoldii.ui.all.MainScreen
import com.example.projetoldii.ui.all.routes.HomeRoute
import com.example.projetoldii.ui.all.routes.ProjectFormRoute
import com.example.projetoldii.ui.all.routes.ProjectKanBanRoute
import com.example.projetoldii.ui.viewmodels.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("Home")
    object CreateProject : Screen("createProject")
    object ProjectKanBan : Screen("project/{id}"){
        fun routeOf(id: Int) = "project/$id"
    }
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel, projectRepo: ProjectRepository, taskRepo: TaskRepository) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { user ->
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

        composable (Screen.Home.route) {
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
                navController = navController,
                taskRepo = taskRepo,
                projectId = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: return@composable
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

    if (user != null)

        HomeRoute(
            projectRepository = projectRepo,
            currentUserId = user.id_user,
            onOpenProject = { projectId ->
                // TODO: nav para a tela do projeto
                navController.navigate(Screen.ProjectKanBan.routeOf(projectId))
            },
            onCreateProject = {
                navController.navigate(Screen.CreateProject.route)
            },
            onLogout = {
                // Importante: não navegar aqui. Só zera o usuário;
                // a navegação para Login é feita pelo guard acima.
                authViewModel.currentUser.value = null
                }
        )
}

@Composable
private fun CreateProjectEntry(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    navController: NavHostController,
) {
    val user = authViewModel.currentUser.value

    // Guard: se o usuário ficar null (logout), navega 1x para Login
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
        onSavedGoBack = { navController.popBackStack() },   // salvar -> volta pra Home
        onLogoutNav = {
            // Confirmado no diálogo: apenas zera o usuário.
            // A navegação para Login acontece pelo guard acima
            authViewModel.currentUser.value = null
        },
        onCancelNav = { navController.popBackStack() }       // cancelar -> volta pra Home
    )
}

@Composable
private fun ProjectKanBanEntry(
    authViewModel: AuthViewModel,
    projectRepo: ProjectRepository,
    taskRepo: TaskRepository,
    navController: NavHostController,
    projectId: Int
) {
    val user = authViewModel.currentUser.value

    // Guard: se deslogar, volta 1x pro Login
    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    if (user == null) return

    // Chame aqui sua Route do Kanban quando criá-la:
    ProjectKanBanRoute (
         projectRepository = projectRepo,
         currentUserId = user.id_user,
         currentUserName = user.nome,
         projectId = projectId,
         taskRepository = taskRepo,
         onBack = { navController.popBackStack() },
         onLogout = { authViewModel.currentUser.value = null },
         onOpenTaskDetail = { taskId -> /* nav p/ detalhe, quando existir */ },
         onCreateTask = { /* nav para form de task */ },
         onCreateTaskType = { /* nav para form de tipo */ },
         onAddProgrammer = { /* nav para form de adicionar programador */ }
    )

    // Enquanto a Route não estiver pronta, você pode deixar um TODO/placeholder aqui.
}
