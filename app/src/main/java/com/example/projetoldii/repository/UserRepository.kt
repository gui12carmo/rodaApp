package com.example.projetoldii.repository

import com.example.projetoldii.UserDao
import com.example.projetoldii.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(
        nome: String,
        username: String,
        password: String,
        email: String?,
        gestor: Boolean
    ): Result<Boolean> = withContext(Dispatchers.IO) {
        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            return@withContext Result.failure(Exception("Username já está em uso."))
        }

        val existingEmail = email?.let { userDao.getUserByEmail(it) }
        if (existingEmail != null) {
            return@withContext Result.failure(Exception("Email já está em uso."))
        }

        val newUser = User(
            nome = nome,
            username = username,
            password = password,
            email = email,
            gestor = gestor,
            created_at = System.currentTimeMillis()
        )

        userDao.insertUser(newUser)
        Result.success(true)
    }

    suspend fun loginUser(identifier: String, password: String): Result<User> =
        withContext(Dispatchers.IO) {
            // O login pode ser feito via username ou email
            val user = userDao.getUserByUsername(identifier)
                ?: userDao.getUserByEmail(identifier)

            if (user == null || user.password != password) {
                return@withContext Result.failure(Exception("Credenciais inválidas."))
            }

            Result.success(user)
        }
}
