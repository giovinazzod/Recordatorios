package com.example.recordatorios.data.local

import androidx.room.TypeConverter
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.EstadoRecordatorio
import com.example.recordatorios.model.Prioridad

// Convertidores de tipos para que Room pueda almacenar enums y objetos en SQLite
class Convertidores {

    @TypeConverter
    fun prioridadATexto(prioridad: Prioridad): String = prioridad.etiqueta

    @TypeConverter
    fun textoAPrioridad(valor: String): Prioridad =
        Prioridad.todas.find { it.etiqueta.equals(valor, ignoreCase = true) } ?: Prioridad.Media

    @TypeConverter
    fun estadoATexto(estado: EstadoRecordatorio): String = estado.etiqueta

    @TypeConverter
    fun textoAEstado(valor: String): EstadoRecordatorio =
        if (valor.equals("Completado", ignoreCase = true)) {
            EstadoRecordatorio.Completado
        } else {
            EstadoRecordatorio.Pendiente
        }

    @TypeConverter
    fun categoriaATexto(categoria: Categoria): String = "${categoria.id}:${categoria.nombre}"

    @TypeConverter
    fun textoACategoria(valor: String): Categoria {
        val partes = valor.split(":")
        if (partes.size == 2) {
            val id = partes[0].toIntOrNull() ?: 0
            val nombre = partes[1]
            return Categoria.todas.find { it.id == id || it.nombre.equals(nombre, ignoreCase = true) }
                ?: Categoria(id, nombre)
        }
        return Categoria.todas.find { it.nombre.equals(valor, ignoreCase = true) }
            ?: Categoria(0, valor)
    }

    @TypeConverter
    fun syncStatusATexto(estado: SyncStatus): String = estado.name

    @TypeConverter
    fun textoASyncStatus(valor: String): SyncStatus =
        SyncStatus.entries.find { it.name == valor } ?: SyncStatus.PENDING
}
