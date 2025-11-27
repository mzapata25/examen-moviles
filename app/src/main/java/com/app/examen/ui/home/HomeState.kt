package com.app.examen.ui.home

data class HomeState(
    val countryQuery: String = "", // Lo que el usuario escribe en el buscador
    val isLoading: Boolean = false, // Para mostrar el spinner de carga
    val errorMessage: String? = null, // Si falla la API
    val lastCountrySearched: String? = null // Para el requerimiento de "último país visto"
    // Aquí agregaremos la lista de datos del COVID más adelante
)