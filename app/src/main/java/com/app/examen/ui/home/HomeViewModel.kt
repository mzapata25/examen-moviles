package com.app.examen.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examen.data.local.UserPreferencesRepository // Import nuevo
import com.app.examen.domain.repository.CovidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CovidRepository,
    private val prefsRepository: UserPreferencesRepository // <--- Inyectamos preferencias
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        // Al iniciar, leemos la persistencia
        viewModelScope.launch {
            val lastCountry = prefsRepository.lastCountry.first() // Leemos solo una vez al inicio
            if (lastCountry.isNotEmpty()) {
                _uiState.update { it.copy(countryQuery = lastCountry) }
                searchCountry() // <--- Ejecutamos la búsqueda automáticamente
            }
        }
    }

    fun onQueryChange(newQuery: String) {
        _uiState.update { it.copy(countryQuery = newQuery) }
    }

    fun searchCountry() {
        val query = _uiState.value.countryQuery
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, covidData = emptyList()) }

            val result = repository.getCovidDataByCountry(query)

            result.onSuccess { data ->
                // GUARDAMOS EN PERSISTENCIA AQUÍ
                prefsRepository.saveLastCountry(query)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        lastCountrySearched = query,
                        covidData = data,
                        countryQuery = "" // Limpiamos el input visualmente si quieres
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error: ${error.message ?: "No se pudieron obtener datos"}"
                    )
                }
            }
        }
    }
}