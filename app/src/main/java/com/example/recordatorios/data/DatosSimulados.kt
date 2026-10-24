package com.example.recordatorios.data

import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.Recordatorio
import com.example.recordatorios.model.Prioridad
import com.example.recordatorios.model.EstadoRecordatorio

object DatosSimulados {
    val estudio = Categoria(1, "Estudio")
    val trabajo = Categoria(2, "Trabajo")
    val personal = Categoria(3, "Personal")

    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Entregar actividad de Programación Mobile",
            categoria = estudio,
            prioridad = Prioridad.Alta,
            descripcion = "Revisar el proyecto y preparar las capturas."
        ),
        Recordatorio(
            id = 2,
            titulo = "Revisar correos",
            categoria = trabajo,
            prioridad = Prioridad.Media
        ),
        Recordatorio(
            id = 3,
            titulo = "Comprar alimentos",
            categoria = personal,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Completado
        ),
        Recordatorio(
            id = 4,
            titulo = "Repasar Kotlin",
            categoria = estudio,
            prioridad = Prioridad.Media
        )
    )

    val categorias: Set<Categoria> =
        recordatorios.map { it.categoria }.toSet()

    val etiquetasPorCategoria: Map<Categoria, String> = mapOf(
        estudio to "Formación y aprendizaje",
        trabajo to "Actividades laborales",
        personal to "Vida cotidiana"
    )

    val pendientes: List<Recordatorio> =
        recordatorios.filter {
            it.estado == EstadoRecordatorio.Pendiente
        }

    val dePrioridadAlta: List<Recordatorio> =
        recordatorios.filter {
            it.prioridad == Prioridad.Alta
        }

    fun comprobarValidaciones(): String {
        val resultado = runCatching {
            Recordatorio(
                id = 99,
                titulo = "   ",
                categoria = estudio,
                prioridad = Prioridad.Alta
            )
        }

        return resultado.exceptionOrNull()?.message
            ?: "No se rechazó el título vacío."
    }
}