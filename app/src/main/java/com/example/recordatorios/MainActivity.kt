package com.example.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recordatorios.data.DatosSimulados
import com.example.recordatorios.model.Recordatorio

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                PantallaRecordatorios()
            }
        }
    }
}

@Composable
fun PantallaRecordatorios() {
    Scaffold(modifier = Modifier.fillMaxSize()) { espacioSeguro ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioSeguro),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Mis recordatorios",
                    /*text = DatosSimulados.comprobarValidaciones(),*/
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text = "${DatosSimulados.pendientes.size} pendientes · " +
                            "${DatosSimulados.dePrioridadAlta.size} de prioridad alta",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            items(
                items = DatosSimulados.recordatorios,
                key = { it.id }
            ) { recordatorio ->
                TarjetaRecordatorio(recordatorio)
            }
        }
    }
}

@Composable
fun TarjetaRecordatorio(recordatorio: Recordatorio) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = recordatorio.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = "Categoría: ${recordatorio.categoria.nombre}")

            DatosSimulados.etiquetasPorCategoria[recordatorio.categoria]?.let {
                    etiqueta ->
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(text = "Prioridad: ${recordatorio.prioridad.etiqueta}")
            Text(text = "Estado: ${recordatorio.estado.etiqueta}")

            recordatorio.descripcion?.let { descripcion ->
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}