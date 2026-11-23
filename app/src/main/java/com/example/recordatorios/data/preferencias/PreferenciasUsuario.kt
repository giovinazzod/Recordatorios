package com.example.recordatorios.data.preferencias

/**
 * Modelo inmutable con las configuraciones del usuario que sobreviven al cierre de la app.
 * Define valores predeterminados seguros por defecto.
 */
data class PreferenciasUsuario(
    val temaOscuro: Boolean = false,
    val ordenarPorPrioridad: Boolean = false,
    val ocultarCompletados: Boolean = false
)