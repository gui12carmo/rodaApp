package com.example.projetoldii.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.User
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    var isLoading = mutableStateOf(false)
    var currentUser = mutableStateOf<User?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null

            val result = loginUserUseCase(identifier, password)
            isLoading.value = false

            result.onSuccess {
                currentUser.value = it
                successMessage.value = "Login realizado com sucesso!"
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }

    fun register(
        nome: String,
        username: String,
        password: String,
        email: String?
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            successMessage.value = null

            val result = registerUserUseCase(nome, username, password, email)
            isLoading.value = false

            result.onSuccess {
                successMessage.value = "Conta criada com sucesso!"
            }.onFailure {
                errorMessage.value = it.message
            }
        }
    }
}
