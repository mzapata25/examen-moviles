package com.app.examen.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.examen.domain.repository.CovidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CovidRepository // Inyectamos el repositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

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
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        lastCountrySearched = query,
                        covidData = data,
                        countryQuery = ""
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