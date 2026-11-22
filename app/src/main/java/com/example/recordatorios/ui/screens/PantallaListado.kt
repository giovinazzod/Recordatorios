package com.example.recordatorios.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.example.recordatorios.ui.components.EstadoCarga
import com.example.recordatorios.ui.components.EstadoError
import com.example.recordatorios.ui.components.EstadoVacio
import com.example.recordatorios.ui.components.TarjetaRecordatorio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListado(
    estado: RecordatoriosUiState,
    onCrearClick: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    onSimularVacio: () -> Unit = {},
    onSimularError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var menuAbierto by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mis Recordatorios") },
                actions = {
                    // Menú para simular estados
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones de prueba"
                        )
                    }
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Recargar lista original") },
                            onClick = {
                                menuAbierto = false
                                onReintentar()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Simular estado vacío") },
                            onClick = {
                                menuAbierto = false
                                onSimularVacio()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Simular estado error") },
                            onClick = {
                                menuAbierto = false
                                onSimularError()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear nuevo recordatorio"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (estado) {
                is RecordatoriosUiState.Cargando -> {
                    EstadoCarga()
                }

                is RecordatoriosUiState.Vacio -> {
                    EstadoVacio(
                        mensaje = "No tenés recordatorios registrados todavía.",
                        accionTexto = "Crear el primero",
                        onAccion = onCrearClick
                    )
                }

                is RecordatoriosUiState.Error -> {
                    EstadoError(
                        mensaje = estado.mensaje,
                        onReintentar = onReintentar
                    )
                }

                is RecordatoriosUiState.Exito -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = estado.recordatorios,
                            key = { it.id }
                        ) { recordatorio ->
                            TarjetaRecordatorio(
                                recordatorio = recordatorio,
                                onClick = { onRecordatorioClick(recordatorio.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}