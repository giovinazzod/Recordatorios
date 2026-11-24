package com.example.recordatorios.data

import com.example.recordatorios.model.Recordatorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecordatoriosEnMemoriaRepository(
    datosIniciales: List<Recordatorio> = DatosSimulados.recordatorios
) : RecordatoriosRepository {

    private val recordatorios = MutableStateFlow<List<Recordatorio>>(datosIniciales)

    override fun observarRecordatorios(): Flow<List<Recordatorio>> = recordatorios.asStateFlow()

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        return recordatorios.value.firstOrNull { it.id == id }
    }

    override suspend fun guardar(recordatorio: Recordatorio) {
        val nuevoRecordatorio = if (recordatorio.id <= 0) {
            val proximoId = (recordatorios.value.maxOfOrNull { it.id } ?: 0) + 1
            recordatorio.copy(id = proximoId)
        } else {
            recordatorio
        }

        recordatorios.update { actuales ->
            val indiceExistente = actuales.indexOfFirst { it.id == nuevoRecordatorio.id }
            if (indiceExistente >= 0) {
                actuales.toMutableList().apply { set(indiceExistente, nuevoRecordatorio) }
            } else {
                actuales + nuevoRecordatorio
            }
        }
    }

    override suspend fun eliminar(recordatorio: Recordatorio) {
        recordatorios.update { actuales -> actuales.filterNot { it.id == recordatorio.id } }
    }

    fun establecerRecordatorios(lista: List<Recordatorio>) {
        recordatorios.value = lista
    }
}
