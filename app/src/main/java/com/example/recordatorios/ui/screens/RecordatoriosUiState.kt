package com.example.recordatorios.ui.screens

import com.example.recordatorios.model.Recordatorio

// Sealed interface para modelar estados mutuamente excluyentes y evitar inconsistencias en la UI
sealed interface RecordatoriosUiState {

    // Proceso de carga inicial
    object Cargando : RecordatoriosUiState

    // Lista de recordatorios obtenida con éxito
    data class Exito(val recordatorios: List<Recordatorio>) : RecordatoriosUiState

    // Sin recordatorios registrados
    object Vacio : RecordatoriosUiState

    // Mensaje descriptivo en caso de error
    data class Error(val mensaje: String) : RecordatoriosUiState
}