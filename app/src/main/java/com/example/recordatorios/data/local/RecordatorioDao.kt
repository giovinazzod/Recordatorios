package com.example.recordatorios.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.example.recordatorios.model.EstadoRecordatorio
import kotlinx.coroutines.flow.Flow

// Operaciones de lectura y escritura sobre la tabla de recordatorios
@Dao
interface RecordatorioDao {

    // Muestra el listado completo cada vez que la tabla sufre modificaciones
    @Query("SELECT * FROM recordatorios ORDER BY id DESC")
    fun observarTodos(): Flow<List<RecordatorioEntity>>

    // Permite observar un registro individual
    @Query("SELECT * FROM recordatorios WHERE id = :id")
    fun observarPorId(id: Int): Flow<RecordatorioEntity?>

    // Consulta puntual por identificador
    @Query("SELECT * FROM recordatorios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): RecordatorioEntity?

    // Consulta reactiva filtrando por estado de la tarea
    @Query("SELECT * FROM recordatorios WHERE estado = :estado ORDER BY id DESC")
    fun observarPorEstado(estado: EstadoRecordatorio): Flow<List<RecordatorioEntity>>

    // Registros modificados o creados localmente que restan sincronizar
    @Query("SELECT * FROM recordatorios WHERE sync_status != 'SYNCED' ORDER BY fecha_creacion ASC")
    suspend fun pendientes(): List<RecordatorioEntity>

    // Busqueda por ID remoto para prevenir inserciones duplicadas al sincronizar
    @Query("SELECT * FROM recordatorios WHERE remote_id = :remoteId")
    suspend fun buscarPorRemoteId(remoteId: String): RecordatorioEntity?

    // Alta de un registro
    @Insert
    suspend fun insertar(recordatorio: RecordatorioEntity): Long

    // Modificación de un registro
    @Update
    suspend fun actualizar(recordatorio: RecordatorioEntity): Int

    // Eliminación de un registro
    @Delete
    suspend fun eliminar(recordatorio: RecordatorioEntity)

    // Eliminación directa por identificador
    @Query("DELETE FROM recordatorios WHERE id = :id")
    suspend fun eliminarPorId(id: Int)
}
