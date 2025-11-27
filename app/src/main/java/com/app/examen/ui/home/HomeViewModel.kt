package com.app.examen.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Aquí inyectaremos los Casos de Uso (Domain) más adelante
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    // Actualiza el texto mientras el usuario escribe
    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(countryQuery = newQuery) }
    }

    // Acción de buscar (Simulada por ahora)
    fun searchCountry() {
        val query = _uiState.value.countryQuery
        if (query.isBlank()) return

        viewModelScope.launch {
            // 1. Estado de carga
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // TODO: Aquí llamaremos al Caso de Uso del Dominio (API)
            delay(2000) // Simulación de red

            // 2. Resultado simulado (Éxito)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    lastCountrySearched = query,
                    countryQuery = ""
                )
            }
        }
    }
}