package com.example.projetoldii

import androidx.room.*
import com.example.projetoldii.data.AddProgrammer
import com.example.projetoldii.data.Project
import com.example.projetoldii.data.Task
import com.example.projetoldii.data.TaskType
import com.example.projetoldii.data.User


@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM User WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String?): User?
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM Project")
    suspend fun getAll(): List<Project>

    @Query("SELECT * FROM Project WHERE id_owner = :ownerId")
    fun observeByOwner(ownerId: Int): kotlinx.coroutines.flow.Flow<List<Project>>

    @Query("SELECT * FROM Project WHERE id_projeto IN (:ids)")
    fun observeByIds(ids: List<Int>): kotlinx.coroutines.flow.Flow<List<Project>>

    @Insert
    suspend fun insert(project: Project)

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

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

    @Query("SELECT * FROM AddProgrammer WHERE id_programador = :userId")
    fun observeByProgrammer(userId: Int): kotlinx.coroutines.flow.Flow<List<AddProgrammer>>

    @Query("SELECT * FROM AddProgrammer WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<AddProgrammer>
}


