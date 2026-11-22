package com.example.recordatorios.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.Recordatorio

// Tarjeta individual para presentar el resumen de un recordatorio en la lista
@Composable
fun TarjetaRecordatorio(
    recordatorio: Recordatorio,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fila superior: Título y Categoría
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = recordatorio.categoria.nombre,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            // Fila inferior: Estado y Prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado: ${recordatorio.estado.etiqueta}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                InsigniaPrioridad(prioridad = recordatorio.prioridad)
            }
        }
    }
}

/* Insignia visual y accesible para la prioridad. */
@Composable
fun InsigniaPrioridad(
    prioridad: Prioridad,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val (colorFondo, colorTexto) = when (prioridad) {
        Prioridad.Alta -> colores.errorContainer to colores.onErrorContainer
        Prioridad.Media -> colores.tertiaryContainer to colores.onTertiaryContainer
        Prioridad.Baja -> colores.surfaceVariant to colores.onSurfaceVariant
    }

    Surface(
        color = colorFondo,
        contentColor = colorTexto,
        shape = MaterialTheme.shapes.small,
        modifier = modifier.semantics {
            contentDescription = "Prioridad ${prioridad.etiqueta}"
        }
    ) {
        Text(
            text = prioridad.etiqueta,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}