package com.example.projetoldii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.MoveTaskUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.repository.UserRepository
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.navigation.AppNavigation
import com.example.projetoldii.ui.viewmodels.AuthViewModel
import com.example.projetoldii.ui.all.viewmodels.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    // instância única do DB para esta Activity
    private val db by lazy { DatabaseProvider.getDatabase(this) }

    // repositório de usuário
    private val userRepository by lazy { UserRepository(db.userDao()) }

    // ViewModel de autenticação
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            loginUserUseCase = LoginUserUseCase(userRepository),
            registerUserUseCase = RegisterUserUseCase(userRepository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // repositório de projetos
        val projectRepo = ProjectRepository(
            projectDao = db.projectDao(),
            addProgrammerDao = db.addProgrammerDao()
        )

        // repositório de tasks
        val taskRepo = TaskRepository(
            taskDao = db.taskDao(),
            taskTypeDao = db.taskTypeDao(),
            addProgrammerDao = db.addProgrammerDao(),
        )

        // use case que move tasks (usado no board / kanban / etc.)
        val moveTaskUseCase = MoveTaskUseCase(taskRepo)

        setContent {
            ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
                Surface {
                    AppNavigation(
                        authViewModel = authViewModel,
                        projectRepo   = projectRepo,
                        taskRepo      = taskRepo,
                        moveTask      = moveTaskUseCase   // 👈 nome bate com o parâmetro
                    )
                }
            }
        }
    }
}