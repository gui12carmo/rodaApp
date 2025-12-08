package com.example.projetoldii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.projetoldii.data.AppDatabase
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import com.example.projetoldii.repository.UserRepository
import com.example.projetoldii.ui.all.ProjetoLDIITheme
import com.example.projetoldii.ui.navigation.AppNavigation
import com.example.projetoldii.ui.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtém a instância do banco local (Room)
        val db = DatabaseProvider.getDatabase(this)
        val userRepository = UserRepository(db.UserDao())

        // Cria o ViewModel principal de autenticação
        val authViewModel = AuthViewModel(
            loginUserUseCase = LoginUserUseCase(userRepository),
            registerUserUseCase = RegisterUserUseCase(userRepository)
        )

        setContent {
            ProjetoLDIITheme(darkTheme = false, dynamicColor = false) {
                Surface {
                    // Chamamos o sistema de navegação
                    AppNavigation(authViewModel)
                }
            }
        }
    }
}
