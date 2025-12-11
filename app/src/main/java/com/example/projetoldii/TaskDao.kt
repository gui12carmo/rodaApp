package com.example.projetoldii

import androidx.room.*
import com.example.projetoldii.data.AddProgrammer
import com.example.projetoldii.data.Project
import com.example.projetoldii.data.Task
import com.example.projetoldii.data.TaskType
import com.example.projetoldii.data.User
import kotlinx.coroutines.flow.Flow


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

    @Query("SELECT * FROM Project WHERE id_projeto = :projectId LIMIT 1")
    fun observeById(projectId: Int): kotlinx.coroutines.flow.Flow<Project?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM Project WHERE id_owner = :ownerId")
    suspend fun getProjectsByOwner(ownerId: Int): List<Project>
}

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task WHERE id_projeto = :projectId")
    fun observeByProject(projectId: Int): kotlinx.coroutines.flow.Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE id_projeto = :projectId AND id_tipoTarefa = :typeId")
    fun observeByProjectAndType(projectId: Int, typeId: Int): kotlinx.coroutines.flow.Flow<List<Task>>

    @Query("""SELECT id_tipoTarefa as typeId, COUNT(*) as total FROM Task WHERE id_projeto = :projectId GROUP BY id_tipoTarefa""")
    fun observeCountsByType(projectId: Int): kotlinx.coroutines.flow.Flow<List<TypeCount>>

    @Update
    suspend fun update(task: Task)

    // POJO para contadores
    data class TypeCount(val typeId: Int?, val total: Int)

    @Insert suspend fun insert(task: Task)
    @Query("SELECT * FROM Task WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<Task>
}

@Dao
interface TaskTypeDao {
    @Insert suspend fun insert(taskType: TaskType)
    @Query("SELECT * FROM TaskType WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<TaskType>

    @Query("SELECT * FROM TaskType WHERE id_projeto = :projectId ORDER BY id_tipoTarefa ASC")
    fun observeByProject(projectId: Int): kotlinx.coroutines.flow.Flow<List<TaskType>>
}

@Dao
interface AddProgrammerDao {
    @Insert suspend fun insert(ap: AddProgrammer)

    @Query("SELECT * FROM AddProgrammer WHERE id_programador = :userId")
    fun observeByProgrammer(userId: Int): kotlinx.coroutines.flow.Flow<List<AddProgrammer>>

    @Query("SELECT * FROM AddProgrammer WHERE id_projeto = :projectId")
    suspend fun getByProject(projectId: Int): List<AddProgrammer>

    @Query("""SELECT COUNT(*) FROM AddProgrammer WHERE id_projeto = :projectId AND id_programador = :userId""")
    fun observeMembershipCount(projectId: Int, userId: Int): Flow<Int>
}


