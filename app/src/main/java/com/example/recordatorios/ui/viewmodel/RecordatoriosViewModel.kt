package com.example.recordatorios.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordatorios.data.ContenedorApp
import com.example.recordatorios.data.RecordatoriosRepository
import com.example.recordatorios.data.preferencias.PreferenciasUsuario
import com.example.recordatorios.data.preferencias.RepositorioPreferencias
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.EstadoRecordatorio
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.Recordatorio
import com.example.recordatorios.ui.screens.RecordatoriosUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Administra el estado de la pantalla y sobrevive a los cambios de configuración como la rotación
class RecordatoriosViewModel(
    private val repositorio: RecordatoriosRepository = ContenedorApp.repositorio,
    private val preferenciasRepo: RepositorioPreferencias = ContenedorApp.preferencias
) : ViewModel() {

    // Flujo de preferencias expuesto para la interfaz
    val preferencias: StateFlow<PreferenciasUsuario> = preferenciasRepo.preferencias
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreferenciasUsuario()
        )

    private val _uiState = MutableStateFlow<RecordatoriosUiState>(RecordatoriosUiState.Cargando)
    val uiState: StateFlow<RecordatoriosUiState> = _uiState.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    init {
        // Al crearse el ViewModel, inicia la carga automática de recordatorios
        cargarRecordatorios()
    }

    /**
     * Carga los recordatorios observando el flujo del repositorio.
     * Métodos auxiliares para probar los distintos estados de la interfaz
     */
    fun cargarRecordatorios(simularError: Boolean = false, simularVacio: Boolean = false) {
        _uiState.update { RecordatoriosUiState.Cargando }

        viewModelScope.launch {
            // Simulamos demora de red / tiempo de carga
            delay(400)

            if (simularError) {
                _uiState.update {
                    RecordatoriosUiState.Error("Error al sincronizar los recordatorios. Verificá tu conexión.")
                }
            } else if (simularVacio) {
                _uiState.update { RecordatoriosUiState.Vacio }
            } else {
                // Combinamos la lista con las preferencias activas del usuario
                combine(repositorio.observarRecordatorios(), preferenciasRepo.preferencias) { lista, prefs ->
                    var resultado = lista

                    // Filtro: ocultar tareas completadas
                    if (prefs.ocultarCompletados) {
                        resultado = resultado.filter { it.estado != EstadoRecordatorio.Completado }
                    }

                    // Ordenamiento: alta primero, luego media y baja
                    if (prefs.ordenarPorPrioridad) {
                        resultado = resultado.sortedByDescending {
                            when (it.prioridad) {
                                Prioridad.Alta -> 3
                                Prioridad.Media -> 2
                                Prioridad.Baja -> 1
                            }
                        }
                    }

                    resultado
                }.collect { procesados ->
                    _uiState.update {
                        if (procesados.isEmpty()) RecordatoriosUiState.Vacio
                        else RecordatoriosUiState.Exito(procesados)
                    }
                }
            }
        }
    }

    // Métodos para modificar las preferencias de forma asincrónica
    fun setTemaOscuro(activo: Boolean) {
        viewModelScope.launch { preferenciasRepo.setTemaOscuro(activo) }
    }

    fun setOrdenarPorPrioridad(activo: Boolean) {
        viewModelScope.launch { preferenciasRepo.setOrdenarPorPrioridad(activo) }
    }

    fun setOcultarCompletados(activo: Boolean) {
        viewModelScope.launch { preferenciasRepo.setOcultarCompletados(activo) }
    }

    fun recargar() = cargarRecordatorios()
    fun simularVacio() = cargarRecordatorios(simularVacio = true)
    fun simularError() = cargarRecordatorios(simularError = true)

    // Guarda un nuevo recordatorio mediante el repositorio y notifica el éxito.
    fun guardarRecordatorio(
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad,
        onExito: () -> Unit
    ) {
        if (_guardando.value) return
        _guardando.update { true }

        viewModelScope.launch {
            delay(500)
            val nuevo = Recordatorio(
                id = 0,
                titulo = titulo,
                categoria = categoria,
                prioridad = prioridad,
                descripcion = descripcion.ifBlank { null }
            )
            repositorio.guardar(nuevo)
            _guardando.update { false }
            onExito()
        }
    }

    // Busca un recordatorio por su identificador para la pantalla de detalle.
    suspend fun obtenerRecordatorioPorId(id: Int): Recordatorio? {
        return repositorio.obtenerPorId(id)
    }

    // Actualizar un recordatorio
    fun actualizarRecordatorio(
        original: Recordatorio,
        titulo: String,
        descripcion: String,
        categoria: Categoria,
        prioridad: Prioridad,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            repositorio.guardar(
                original.copy(
                    titulo = titulo,
                    descripcion = descripcion.ifBlank { null },
                    categoria = categoria,
                    prioridad = prioridad
                )
            )
            onExito()
        }
    }

    // Eliminar recordatorio
    fun eliminarRecordatorio(
        recordatorio: Recordatorio,
        onExito: () -> Unit
    ) {
        viewModelScope.launch {
            repositorio.eliminar(recordatorio)
            onExito()
        }
    }
}