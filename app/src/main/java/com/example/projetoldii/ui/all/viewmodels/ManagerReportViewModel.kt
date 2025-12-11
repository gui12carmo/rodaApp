package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.TaskDao
import com.example.projetoldii.TaskWithProgrammer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

// Modelo que a tela do gestor usa
data class ManagerReportItem(
    val title: String,
    val programmerName: String,
    val expectedDays: Int,
    val deliveredDays: Int
)

class ManagerReportViewModel(
    private val taskDao: TaskDao,
    private val projectId: Int
) : ViewModel() {

    private val _items = MutableStateFlow<List<ManagerReportItem>>(emptyList())
    val items: StateFlow<List<ManagerReportItem>> = _items.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            taskDao.observeCompletedTasksForProject(projectId)
                .collectLatest { list ->
                    _items.value = list.map { it.toManagerReportItem() }
                }
        }
    }
}

// ---------- helpers ----------

private fun TaskWithProgrammer.toManagerReportItem(): ManagerReportItem {
    val task = this.task
    val expectedDays = daysBetween(task.dt_prevista_inicio, task.dt_prevista_final)
    val deliveredDays = daysBetween(task.dt_real_inicio, task.dt_real_fim)

    return ManagerReportItem(
        title = task.descricao ?: "Nome da Task",
        programmerName = programmerName ?: "Sem programador",
        expectedDays = expectedDays,
        deliveredDays = deliveredDays
    )
}

private fun daysBetween(startMillis: Long?, endMillis: Long?): Int {
    if (startMillis == null || endMillis == null) return 0
    val diff = endMillis - startMillis
    if (diff <= 0) return 0
    val dayMillis = 24L * 60L * 60L * 1000L
    return max(1, (diff / dayMillis).toInt())
}