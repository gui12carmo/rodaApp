package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.UserRepository

class RegisterUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(
        nome: String,
        username: String,
        password: String,
        email: String?
    ) = repository.registerUser(nome, username, password, email)
}
