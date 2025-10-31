package com.example.projetoldii

import androidx.room.*
import com.example.projetoldii.data.AddProgrammer
import com.example.projetoldii.data.Project
import com.example.projetoldii.data.Task
import com.example.projetoldii.data.TaskType
import com.example.projetoldii.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    suspend fun getAll(): List<Project>

    @Insert
    suspend fun insert(project: Project)

    @Query("SELECT * FROM Project WHERE id_owner = :ownerId")
    suspend fun getProjectsByOwner(ownerId: Int): List<Project>
}

@Dao
interface TaskDao {
    @Insert suspend fun insert(task: Task)
    @Query("SELECT * FROM Task WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<Task>
}

@Dao
interface TaskTypeDao {
    @Insert suspend fun insert(taskType: TaskType)
    @Query("SELECT * FROM TaskType WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<TaskType>
}

@Dao
interface AddProgrammerDao {
    @Insert suspend fun insert(ap: AddProgrammer)
    @Query("SELECT * FROM AddProgrammer WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<AddProgrammer>
}


