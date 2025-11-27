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
import androidx.lifecycle.viewmodel.compose.viewModel // O hiltViewModel() si ya configuraste Hilt

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
                        HomeContent(state.lastCountrySearched)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContent(lastCountry: String?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (lastCountry != null) {
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
            Text("Aquí se mostrarán las estadísticas...")
        } else {
            Text(
                text = "Ingresa un país para comenzar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}