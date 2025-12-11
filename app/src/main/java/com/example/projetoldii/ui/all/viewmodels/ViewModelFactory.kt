package com.example.projetoldii.ui.all.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projetoldii.TaskDao

class ProgrammerReportViewModelFactory(
    private val taskDao: TaskDao,
    private val userId: Int,
    private val projectId: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgrammerReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgrammerReportViewModel(
                taskDao = taskDao,
                userId = userId,
                projectId = projectId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}