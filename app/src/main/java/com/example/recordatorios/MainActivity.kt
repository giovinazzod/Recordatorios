package com.example.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.example.recordatorios.ui.navigation.AppNavHost
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recordatorios.ui.navigation.AppNavHost
import com.example.recordatorios.ui.viewmodel.RecordatoriosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: RecordatoriosViewModel = viewModel()
            val preferencias by viewModel.preferencias.collectAsStateWithLifecycle()

            // Aplica el esquema de color según la preferencia almacenada en DataStore
            val esquemaColor = if (preferencias.temaOscuro) {
                darkColorScheme()
            } else {
                lightColorScheme()
            }

            MaterialTheme(colorScheme = esquemaColor) {
                AppNavHost(viewModel = viewModel)
            }
        }
    }
}