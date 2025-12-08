package com.example.projetoldii.ui.all.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetoldii.R
import com.example.projetoldii.data.User
import com.example.projetoldii.ui.viewmodels.AuthViewModel

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit

) {
    val isLoading by viewModel.isLoading
    val error by viewModel.errorMessage
    val success by viewModel.successMessage

    var nome by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }


    val currentUser by viewModel.currentUser
    LaunchedEffect(currentUser) {
        if (success != null) onRegisterSuccess()
    }

    DisposableEffect(Unit) {
        viewModel.clearMessages()
        onDispose {
            viewModel.clearMessages()
        }
    }

    fun validate(): Boolean{
        nameError = if (nome.isBlank()) "Nome Completo é obrigatório" else null
        usernameError = when {
            username.isBlank() -> "Username é obrigatório"
            !username.matches(Regex("[a-zA-Z0-9]+")) -> "Username deve conter apenas letras e números"
            else -> null
        }
        passwordError = if (password.length < 6) "Minimo de 6 caracteres" else null

        return nameError == null && usernameError == null && passwordError == null
    }

    fun register() {
        if (!validate()) return
        viewModel.register(
            nome = nome,
            username = username,
            password = password,
            email = email.ifBlank { null },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_roda),
                contentDescription = "Logo Roda",
                modifier = Modifier
                    .size(200.dp)
            )

            Text(
                "Registo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it; if (nameError != null) nameError = null },
                label = { Text("Nome Completo*") },
                singleLine = true,
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it; if (usernameError != null) usernameError = null },
                label = { Text("Username*") },
                singleLine = true,
                isError = usernameError != null,
                supportingText = { usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; if (passwordError != null) passwordError = null },
                label = { Text("Password") },
                singleLine = true,
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }},
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.VisibilityOff
                            else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible) "Esconder password"
                            else "Mostrar password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = ::register,
                enabled = !isLoading,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(if (isLoading) "Registrando..." else "REGISTAR")
            }

            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }

            if (success != null) {
                Text(success!!, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(40.dp))

            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(Modifier.height(16.dp))

            val annotated = buildAnnotatedString {
                append("Possui Registo? ")
                pushStringAnnotation(tag = "register", annotation = "register")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                ) { append("Entrar") }
                pop()
            }

            ClickableText(
                text = annotated,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { offset ->
                    annotated.getStringAnnotations("register", offset, offset).firstOrNull()?.let {
                        onNavigateToLogin()
                    }
                }
            )

        }
    }
}