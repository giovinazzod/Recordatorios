package com.example.recordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recordatorios.model.Recordatorio
import com.example.recordatorios.ui.screens.PantallaCreacion
import com.example.recordatorios.ui.screens.PantallaDetalle
import com.example.recordatorios.ui.screens.PantallaListado
import com.example.recordatorios.ui.viewmodel.RecordatoriosViewModel

// Grafo de navegación central que vincula las tres pantallas principales
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: RecordatoriosViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "listado",
        modifier = modifier
    ) {
        // Destino principal: lista de recordatorios
        composable(route = "listado") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val prefs by viewModel.preferencias.collectAsStateWithLifecycle()

            PantallaListado(
                estado = uiState,
                preferencias = prefs,
                onTemaOscuroChange = { viewModel.setTemaOscuro(it) },
                onOrdenarPrioridadChange = { viewModel.setOrdenarPorPrioridad(it) },
                onOcultarCompletadosChange = { viewModel.setOcultarCompletados(it) },
                onCrearClick = { navController.navigate("creacion") },
                onRecordatorioClick = { id -> navController.navigate("detalle/$id") },
                onReintentar = { viewModel.recargar() },
                onSimularVacio = { viewModel.simularVacio() },
                onSimularError = { viewModel.simularError() }
            )
        }

        // Destino: formulario de alta
        composable(route = "creacion") {
            val guardando by viewModel.guardando.collectAsStateWithLifecycle()

            PantallaCreacion(
                guardando = guardando,
                onGuardar = { titulo, descripcion, categoria, prioridad ->
                    viewModel.guardarRecordatorio(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        prioridad = prioridad,
                        onExito = {
                            // Regresa a la pantalla anterior liberando la actual del back stack
                            navController.popBackStack()
                        }
                    )
                },
                onCancelar = { navController.popBackStack() }
            )
        }

        // Destino: detalle recibiendo únicamente el identificador como argumento
        composable(
            route = "detalle/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val recordatorioId = backStackEntry.arguments?.getInt("id") ?: 0

            var recordatorio by remember { mutableStateOf<Recordatorio?>(null) }

            // Buscamos el registro mediante su id
            LaunchedEffect(recordatorioId) {
                recordatorio = viewModel.obtenerRecordatorioPorId(recordatorioId)
            }

            PantallaDetalle(
                recordatorio = recordatorio,
                onVolver = { navController.popBackStack() },
                onEliminar = {
                    recordatorio?.let { seleccionado ->
                        viewModel.eliminarRecordatorio(seleccionado) {
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}