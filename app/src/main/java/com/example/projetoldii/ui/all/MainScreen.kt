package com.example.projetoldii.ui.all

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.projetoldii.data.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(db: AppDatabase) {
    val userDao = db.userDao()
    val projectDao = db.projectDao()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var users by remember { mutableStateOf(listOf<User>()) }
    var projects by remember { mutableStateOf(listOf<Project>()) }

    LaunchedEffect(Unit) {
        users = userDao.getAll()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Criar Usuário", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        Button(
            onClick = {
                scope.launch {
                    userDao.insert(
                        User(
                            nome = name,
                            username = username,
                            password = "123",
                            email = email,
                            created_at = System.currentTimeMillis()
                        )
                    )
                    users = userDao.getAll()
                    name = ""; username = ""; email = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Adicionar Usuário")
        }

        Spacer(Modifier.height(16.dp))

        Text("👥 Usuários cadastrados:")
        LazyColumn {
            items(users.size) { i ->
                val user = users[i]
                Text("• ${user.nome} (${user.username})")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Parte de Projeto
        var projectName by remember { mutableStateOf("") }
        var projectDesc by remember { mutableStateOf("") }

        Text("📁 Criar Projeto", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Nome do Projeto") })
        OutlinedTextField(value = projectDesc, onValueChange = { projectDesc = it }, label = { Text("Descrição") })

        if (users.isNotEmpty()) {
            Button(
                onClick = {
                    scope.launch {
                        val ownerId = users.first().id_user // usa o primeiro user como dono
                        projectDao.insert(
                            Project(
                                nome = projectName,
                                descricao = projectDesc,
                                id_owner = ownerId,
                                created_at = System.currentTimeMillis()
                            )
                        )
                        projects = projectDao.getProjectsByOwner(ownerId)
                        projectName = ""; projectDesc = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Adicionar Projeto")
            }
        }

        LazyColumn {
            items(projects.size) { i ->
                val proj = projects[i]
                Text("• ${proj.nome} (${proj.descricao})")
            }
        }
    }
}
