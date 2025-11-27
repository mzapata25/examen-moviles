package com.app.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.examen.ui.home.HomeScreen
import com.app.examen.ui.theme.ExamenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Requerido por Hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamenTheme {
                // Llamamos a nuestra pantalla de inicio
                HomeScreen()
            }
        }
    }
}