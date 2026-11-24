package com.example.recordatorios.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.recordatorios.model.Categoria
import com.example.recordatorios.model.EstadoRecordatorio
import com.example.recordatorios.model.Prioridad

// Estado de sincronizacion de cada registro para la estrategia local-first
enum class SyncStatus {
    SYNCED,
    PENDING,
    ERROR
}

// Representa la tabla de recordatorios en la base de datos local SQLite
@Entity(
    tableName = "recordatorios",
    indices = [Index(value = ["remote_id"], unique = true)]
)
data class RecordatorioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "remote_id")
    val remoteId: String? = null,
    val titulo: String,
    val descripcion: String? = null,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING
)
