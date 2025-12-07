package com.example.projetoldii.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val id_user: Int = 0,
    val nome: String,
    val username: String,
    val password: String,
    val email: String?,
    val created_at: Long,
)

@Entity(
    tableName = "Project",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id_user"],
            childColumns = ["id_owner"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Project(
    @PrimaryKey(autoGenerate = true) val id_projeto: Int = 0,
    val nome: String,
    val descricao: String?,
    val id_owner: Int,
    val created_at: Long
)

@Entity(
    tableName = "TaskType"
)
data class TaskType(
    @PrimaryKey(autoGenerate = true) val id_tipoTarefa: Int = 0,
    val id_projeto: Int?,
    val nome: String,
    val descricao: String?,
    val status: Boolean
)

@Entity(
    tableName = "Task",
    foreignKeys = [
        ForeignKey(entity = Project::class, parentColumns = ["id_projeto"], childColumns = ["id_projeto"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id_user"], childColumns = ["id_programador"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TaskType::class, parentColumns = ["id_tipoTarefa"], childColumns = ["id_tipoTarefa"], onDelete = ForeignKey.SET_NULL)
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id_tarefa: Int = 0,
    val id_projeto: Int,
    val id_owner: Int,
    val id_programador: Int?,
    val ordem_execucao: Int?,
    val descricao: String?,
    val dt_prevista_inicio: Long?,
    val dt_prevista_final: Long?,
    val id_tipoTarefa: Int?,
    val story_point: Int?,
    val dt_real_inicio: Long?,
    val dt_real_fim: Long?,
    val status: String?,
    val created_at: Long
)

@Entity(
    tableName = "AddProgrammer",
    foreignKeys = [
        ForeignKey(entity = Project::class, parentColumns = ["id_projeto"], childColumns = ["id_projeto"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = User::class, parentColumns = ["id_user"], childColumns = ["id_programador"], onDelete = ForeignKey.CASCADE)
    ]
)
data class AddProgrammer(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val id_projeto: Int,
    val id_owner: Int,
    val id_programador: Int,
    val nivel_experiencia: String?,
    val departamento: String?,
    val added_at: Long
)
