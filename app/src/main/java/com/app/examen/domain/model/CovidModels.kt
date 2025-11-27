package com.app.examen.domain.model

// Modelo limpio para la UI
data class CovidReport(
    val region: String,
    val dailyStats: List<DailyStat> // Ya transformada a lista ordenada
)

data class DailyStat(
    val date: String,
    val total: Int,
    val newCases: Int
)