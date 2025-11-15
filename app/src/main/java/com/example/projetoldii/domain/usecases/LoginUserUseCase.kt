package com.example.projetoldii.domain.usecases

import com.example.projetoldii.repository.UserRepository

class LoginUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(identifier: String, password: String) =
        repository.loginUser(identifier, password)
}
