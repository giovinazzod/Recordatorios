package com.example.recordatorios.data

import com.example.recordatorios.data.local.RecordatorioEntity
import com.example.recordatorios.data.local.SyncStatus
import com.example.recordatorios.model.Recordatorio

// Conversion de la entidad de Room al modelo de dominio que utiliza la interfaz
fun RecordatorioEntity.aDominio(): Recordatorio = Recordatorio(
    id = id,
    titulo = titulo,
    categoria = categoria,
    prioridad = prioridad,
    estado = estado,
    descripcion = descripcion
)

// Conversion del modelo de dominio a entidad de Room para su almacenamiento
fun Recordatorio.aEntity(
    syncStatus: SyncStatus = SyncStatus.PENDING,
    remoteId: String? = null
): RecordatorioEntity = RecordatorioEntity(
    id = id,
    remoteId = remoteId,
    titulo = titulo,
    descripcion = descripcion,
    categoria = categoria,
    prioridad = prioridad,
    estado = estado,
    syncStatus = syncStatus
)
