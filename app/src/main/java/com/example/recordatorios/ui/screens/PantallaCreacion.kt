package com.example.recordatorios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.Prioridad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCreacion(
    onGuardar: (titulo: String, descripcion: String, categoria: Categoria, prioridad: Prioridad) -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    guardando: Boolean = false
) {
    // Estados locales para controlar los campos del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf(Categoria.todas.firstOrNull()) }
    var prioridadSeleccionada by remember { mutableStateOf<Prioridad?>(Prioridad.Media) }
    var tituloTocado by remember { mutableStateOf(false) }

    // Reglas de validación: título >= 4 caracteres y descripción <= 200
    val tituloLimpio = titulo.trim()
    val errorTitulo = when {
        tituloTocado && tituloLimpio.isEmpty() -> "El título no puede estar vacío"
        tituloTocado && tituloLimpio.length < 4 -> "El título debe contener al menos 4 caracteres"
        else -> null
    }

    val esDescripcionValida = descripcion.length <= 200
    val errorDescripcion = if (!esDescripcionValida) "No puede superar los 200 caracteres" else null

    val esFormularioValido = tituloLimpio.length >= 4 &&
            esDescripcionValida &&
            categoriaSeleccionada != null &&
            prioridadSeleccionada != null

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Recordatorio") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    tituloTocado = true
                },
                label = { Text("Título *") },
                isError = errorTitulo != null,
                supportingText = {
                    if (errorTitulo != null) {
                        Text(errorTitulo)
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        if (errorTitulo != null) error(errorTitulo)
                    }
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                isError = errorDescripcion != null,
                supportingText = {
                    Text(
                        text = errorDescripcion ?: "${descripcion.length} / 200 caracteres",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            // Selector de Categoría
            Text(text = "Categoría *", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Categoria.todas.forEach { categoria ->
                    FilterChip(
                        selected = categoria == categoriaSeleccionada,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria.nombre) }
                    )
                }
            }

            // Selector de Prioridad
            Text(text = "Prioridad *", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Prioridad.todas.forEach { prioridad ->
                    FilterChip(
                        selected = prioridad == prioridadSeleccionada,
                        onClick = { prioridadSeleccionada = prioridad },
                        label = { Text(prioridad.etiqueta) }
                    )
                }
            }

            // Botones Guardar y Cancelar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelar,
                    modifier = Modifier.weight(1f),
                    enabled = !guardando
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        val categoria = categoriaSeleccionada
                        val prioridad = prioridadSeleccionada
                        if (esFormularioValido && categoria != null && prioridad != null) {
                            onGuardar(tituloLimpio, descripcion.trim(), categoria, prioridad)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = esFormularioValido && !guardando
                ) {
                    Text(if (guardando) "Guardando..." else "Guardar")
                }
            }
        }
    }
}