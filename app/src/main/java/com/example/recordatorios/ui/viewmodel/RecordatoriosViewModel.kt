package com.example.recordatorios.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordatorios.data.RecordatoriosEnMemoriaRepository
import com.example.recordatorios.data.RecordatoriosRepository
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.Recordatorio
import com.example.recordatorios.ui.screens.RecordatoriosUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Administra el estado de la pantalla y sobrevive a los cambios de configuración como la rotación
class RecordatoriosViewModel(
    private val repositorio: RecordatoriosRepository = RecordatoriosEnMemoriaRepository()
) : ViewModel() {

    // MutableStateFlow encapsulado privadamente y expuesto como StateFlow de solo lectura
    private val _uiState = MutableStateFlow<RecordatoriosUiState>(RecordatoriosUiState.Cargando)
    val uiState: StateFlow<RecordatoriosUiState> = _uiState.asStateFlow()

    // Estado para indicar si hay una operación de guardado en curso
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
            // Simulamos demora de red
            delay(400)

            if (simularError) {
                _uiState.update {
                    RecordatoriosUiState.Error("Error al sincronizar los recordatorios. Verificá tu conexión.")
                }
            } else if (simularVacio) {
                _uiState.update { RecordatoriosUiState.Vacio }
            } else {
                repositorio.observarRecordatorios().collect { lista ->
                    _uiState.update {
                        if (lista.isEmpty()) {
                            RecordatoriosUiState.Vacio
                        } else {
                            RecordatoriosUiState.Exito(lista)
                        }
                    }
                }
            }
        }
    }

    fun recargar() = cargarRecordatorios()
    fun simularVacio() = cargarRecordatorios(simularVacio = true)
    fun simularError() = cargarRecordatorios(simularError = true)

    /**
     * Guarda un nuevo recordatorio mediante el repositorio y notifica el éxito.
     */
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

            val nuevoId = repositorio.generarNuevoId()

            val nuevo = Recordatorio(
                id = nuevoId,
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

    /**
     * Busca un recordatorio por su identificador para la pantalla de detalle.
     */
    suspend fun obtenerRecordatorioPorId(id: Int): Recordatorio? {
        return repositorio.obtenerPorId(id)
    }
}