package com.app.examen.ui.home

import com.app.examen.domain.model.CovidReport

data class HomeState(
    val countryQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val lastCountrySearched: String? = null,
    val covidData: List<CovidReport> = emptyList() // Nueva lista de datos
)