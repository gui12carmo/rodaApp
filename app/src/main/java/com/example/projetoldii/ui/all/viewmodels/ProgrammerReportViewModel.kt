package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetoldii.TaskDao
import com.example.projetoldii.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.max

data class ProgrammerReportItem(
    val title: String,
    val expectedDays: Int,
    val deliveredDays: Int
)

class ProgrammerReportViewModel(
    private val taskDao: TaskDao,
    private val userId: Int,
    private val projectId: Int
) : ViewModel() {

    private val _items = MutableStateFlow<List<ProgrammerReportItem>>(emptyList())
    val items: StateFlow<List<ProgrammerReportItem>> = _items.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            taskDao.observeCompletedTasksForProgrammer(
                userId = userId,
                projectId = projectId
            ).collectLatest { tasks ->
                _items.value = tasks.map { it.toProgrammerReportItem() }
            }
        }
    }
}

// 🔹 Função de extensão para converter Task -> ProgrammerReportItem
private fun Task.toProgrammerReportItem(): ProgrammerReportItem {
    val expectedDays = daysBetween(dt_prevista_inicio, dt_prevista_final)
    val deliveredDays = daysBetween(dt_real_inicio, dt_real_fim)

    return ProgrammerReportItem(
        title = descricao ?: "Nome da Task",
        expectedDays = expectedDays,
        deliveredDays = deliveredDays
    )
}

/**
 * Calcula diferença em DIAS entre dois timestamps em millis.
 * Se algum for null ou inválido, devolve 0.
 */
private fun daysBetween(startMillis: Long?, endMillis: Long?): Int {
    if (startMillis == null || endMillis == null) return 0
    val diff = endMillis - startMillis
    if (diff <= 0) return 0
    val dayMillis = 24L * 60L * 60L * 1000L
    return max(1, (diff / dayMillis).toInt())
}