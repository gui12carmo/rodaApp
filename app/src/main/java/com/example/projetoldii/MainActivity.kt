package com.example.projetoldii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels                // <- IMPORTA O DELEGATE "by viewModels"
import androidx.compose.material3.Surface
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import com.example.projetoldii.repository.ProjectRepository
import com.example.projetoldii.repository.TaskRepository
import com.example.projetoldii.repository.UserRepository
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.navigation.AppNavigation
import com.example.projetoldii.ui.viewmodels.AuthViewModel
import com.example.projetoldii.ui.all.viewmodels.AuthViewModelFactory

class MainActivity : ComponentActivity() {

    private val db by lazy { DatabaseProvider.getDatabase(this) }
    private val userRepository by lazy { UserRepository(db.userDao()) }

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(
            loginUserUseCase = LoginUserUseCase(userRepository),
            registerUserUseCase = RegisterUserUseCase(userRepository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val db = DatabaseProvider.getDatabase(this)
        val projectRepo = ProjectRepository(
            projectDao = db.projectDao(),
            addProgrammerDao = db.addProgrammerDao()
        )

        val taskRepo = TaskRepository(
            db.taskDao(),
            db.taskTypeDao(),
            addProgrammerDao = db.addProgrammerDao()
        )



        setContent {
            ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
                Surface {
                    AppNavigation(authViewModel = authViewModel, projectRepo = projectRepo, taskRepo = taskRepo)
                }
            }
        }
    }
}
