package com.example.recordatorios.data

import com.example.recordatorios.model.Recordatorio
import kotlinx.coroutines.flow.Flow

interface RecordatoriosRepository {
    fun observarRecordatorios(): Flow<List<Recordatorio>>

    suspend fun obtenerPorId(id: Int): Recordatorio?

    suspend fun guardar(recordatorio: Recordatorio)

    suspend fun eliminar(recordatorio: Recordatorio)
}
