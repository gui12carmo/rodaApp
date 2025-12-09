package com.example.projetoldii.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetoldii.DatabaseProvider
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.ui.all.screens.LoginScreen
import com.example.projetoldii.ui.all.screens.RegisterScreen
import com.example.projetoldii.ui.all.MainScreen
import com.example.projetoldii.ui.all.routes.HomeRoute
import com.example.projetoldii.ui.viewmodels.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("Home")
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
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

        composable(Screen.Home.route) {
            val context = LocalContext.current
            val db = DatabaseProvider.getDatabase(context)

            // Repositório para a Home (Room local)
            val projectRepo = ProjectRepository(
                projectDao = db.projectDao(),
                addProgrammerDao = db.addProgrammerDao()
            )

            // Se por algum motivo chegarmos aqui sem user logado, volta pro Login
            val user = authViewModel.currentUser.value
            if (user == null) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                return@composable
            }

            HomeRoute(
                projectRepository = projectRepo,
                currentUserId = user.id_user,
                onOpenProject = { projectId ->
                    // TODO: nav para a tela do projeto
                    // navController.navigate("project/$projectId")
                },
                onCreateProject = {
                    // TODO: nav para a tela de criar projeto
                    // navController.navigate(Screen.CreateProject.route)
                },
                onLogout = {
                    // se tiver um método de logout, chame-o; senão, zere o estado e volte ao Login
                    authViewModel.currentUser.value = null
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
