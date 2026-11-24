package com.example.recordatorios.data

import com.example.recordatorios.data.local.RecordatorioDao
import com.example.recordatorios.data.local.SyncStatus
import com.example.recordatorios.model.Recordatorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Implementacion del repositorio con persistencia en SQLite mediante Room
class RepositorioRoom(
    private val dao: RecordatorioDao
) : RecordatoriosRepository {

    // Emite reactivamente la lista de recordatorios transformados a modelo de dominio
    override fun observarRecordatorios(): Flow<List<Recordatorio>> =
        dao.observarTodos().map { lista ->
            lista.map { it.aDominio() }
        }

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        return dao.obtenerPorId(id)?.aDominio()
    }

    override suspend fun guardar(recordatorio: Recordatorio) {
        val entidad = recordatorio.aEntity(syncStatus = SyncStatus.PENDING)

        if (recordatorio.id == 0) {
            dao.insertar(entidad)
        } else {
            dao.actualizar(entidad)
        }
    }

    override suspend fun eliminar(recordatorio: Recordatorio) {
        dao.eliminarPorId(recordatorio.id)
    }
}
