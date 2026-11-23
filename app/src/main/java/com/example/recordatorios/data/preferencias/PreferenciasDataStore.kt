package com.example.recordatorios.data.preferencias

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Una sola instancia a nivel superior mediante delegado
private const val NOMBRE_DATASTORE = "recordatorios_preferencias"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NOMBRE_DATASTORE)

class PreferenciasDataStore(private val context: Context) : RepositorioPreferencias {

    private object Claves {
        val TEMA_OSCURO = booleanPreferencesKey("tema_oscuro")
        val ORDENAR_POR_PRIORIDAD = booleanPreferencesKey("ordenar_por_prioridad")
        val OCULTAR_COMPLETADOS = booleanPreferencesKey("ocultar_completados")
    }

    // Exposición reactiva mediante Flow transformando los datos al modelo propio
    override val preferencias: Flow<PreferenciasUsuario> = context.dataStore.data
        .catch { excepcion ->
            // Ante error de lectura de disco (IOException), emitimos valores por defecto
            if (excepcion is IOException) {
                emit(emptyPreferences())
            } else {
                throw excepcion
            }
        }
        .map { preferences ->
            PreferenciasUsuario(
                temaOscuro = preferences[Claves.TEMA_OSCURO] ?: false,
                ordenarPorPrioridad = preferences[Claves.ORDENAR_POR_PRIORIDAD] ?: false,
                ocultarCompletados = preferences[Claves.OCULTAR_COMPLETADOS] ?: false
            )
        }

    // Funciones de modificación suspendidas (atómicas mediante edit)
    override suspend fun setTemaOscuro(activo: Boolean) {
        context.dataStore.edit { it[Claves.TEMA_OSCURO] = activo }
    }

    override suspend fun setOrdenarPorPrioridad(activo: Boolean) {
        context.dataStore.edit { it[Claves.ORDENAR_POR_PRIORIDAD] = activo }
    }

    override suspend fun setOcultarCompletados(activo: Boolean) {
        context.dataStore.edit { it[Claves.OCULTAR_COMPLETADOS] = activo }
    }
}