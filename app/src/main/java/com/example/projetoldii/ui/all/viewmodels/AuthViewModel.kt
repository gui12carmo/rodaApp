package com.example.projetoldii.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.data.User
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    //Estados da UI
    var isLoading = mutableStateOf(false)
    var currentUser = mutableStateOf<User?>(null)
    var errorMessage = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf<String?>(null)

    fun clearMessages() {
        errorMessage.value = null
        successMessage.value = null
    }

    private fun showTransientError(message: String, millis: Long = 3000 ) {
        errorMessage.value = message
        viewModelScope.launch {
            delay(millis)
            if (errorMessage.value == message) errorMessage.value = null
        }
    }

    private fun showTransientSuccess(message: String, millis: Long = 3000){
        successMessage.value = message
        viewModelScope.launch {
            delay(millis)
            if (successMessage.value == message) successMessage.value = null
        }
    }

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

    fun register(nome: String, username: String, password: String, email: String?) {
        viewModelScope.launch {
            isLoading.value = true
            clearMessages()

            registerUserUseCase(nome, username, password, email)
                .onSuccess { created ->
                    if (created) {
                        loginUserUseCase(username, password)
                            .onSuccess { user ->
                                currentUser.value = user
                                showTransientSuccess("Conta criada com sucesso!")
                            }
                            .onFailure {
                                showTransientError("Conta criada, mas falhou o login automático.")
                            }
                    } else {
                        showTransientError("Não foi possível criar a conta.")
                    }
                }
                .onFailure {
                    showTransientError(it.message ?: "Falha ao criar conta.")
                }

            isLoading.value = false
        }
    }

}
