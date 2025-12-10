package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projetoldii.domain.usecases.LoginUserUseCase
import com.example.projetoldii.domain.usecases.RegisterUserUseCase
import com.example.projetoldii.ui.viewmodels.AuthViewModel

class AuthViewModelFactory(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModelProvider.Factory
 {

     @Suppress("UNCHECKED_CAST")
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel (loginUserUseCase, registerUserUseCase) as T
         }
         throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
     }
}