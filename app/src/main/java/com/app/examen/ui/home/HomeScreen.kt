package com.app.examen.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.examen.domain.model.CovidReport
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // viewModel: HomeViewModel = hiltViewModel() // Descomentar cuando Hilt esté listo
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("COVID-19 Tracker") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Sección de Búsqueda ---
            OutlinedTextField(
                value = state.countryQuery,
                onValueChange = { viewModel.onQueryChange(it) },
                label = { Text("Buscar país (ej. Canada)") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        viewModel.searchCountry()
                        keyboardController?.hide()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.searchCountry()
                    keyboardController?.hide()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Buscar Datos")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Manejo de Estados ---
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator()
                    }
                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage ?: "Error desconocido",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {
                        // Contenido principal (Resultados)
                        HomeContent(
                            lastCountry = state.lastCountrySearched,
                            data = state.covidData
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContent(
    lastCountry: String?,
    data: List<CovidReport> // Nuevo parámetro
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (lastCountry != null) {
            // Tarjeta de "Última búsqueda" (Igual que tenías)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Última búsqueda:",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = lastCountry,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lógica de visualización de datos
            if (data.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(data) { report ->
                        RegionCard(report)
                    }
                }
            } else {
                Text("No hay datos para mostrar o no se encontraron resultados.")
            }
        } else {
            Text(
                text = "Ingresa un país para comenzar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Composable auxiliar para pintar cada Región
@Composable
fun RegionCard(report: CovidReport) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Título de la Región
            Text(
                text = report.region,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Mostramos solo el dato más reciente (el primero de la lista ordenada)
            if (report.dailyStats.isNotEmpty()) {
                val latest = report.dailyStats.first()

                Text(
                    text = "Fecha: ${latest.date}",
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(label = "Casos Totales", value = latest.total.toString())
                    StatItem(label = "Nuevos Casos", value = latest.newCases.toString())
                }
            } else {
                Text("Sin estadísticas disponibles")
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}