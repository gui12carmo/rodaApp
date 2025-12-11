package com.example.projetoldii.ui.all.components

// CSV para PROGRAMADOR (não tem coluna de programador)
fun buildProgrammerReportCsv(tasks: List<TaskReportUi>): String {
    val header = "titulo;dias_previstos;dias_entregues\n"
    val rows = tasks.joinToString(separator = "\n") { t ->
        "${t.title};${t.expectedDays};${t.deliveredDays}"
    }
    return header + rows
}

// CSV para GESTOR (inclui nome do programador)
fun buildManagerReportCsv(tasks: List<TaskReportUi>): String {
    val header = "titulo;programador;dias_previstos;dias_entregues\n"
    val rows = tasks.joinToString(separator = "\n") { t ->
        val programmer = t.programmerName ?: ""
        "${t.title};$programmer;${t.expectedDays};${t.deliveredDays}"
    }
    return header + rows
}