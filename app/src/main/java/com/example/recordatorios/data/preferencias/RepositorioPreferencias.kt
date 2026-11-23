package com.example.recordatorios.data.preferencias

import kotlinx.coroutines.flow.Flow

interface RepositorioPreferencias {
    val preferencias: Flow<PreferenciasUsuario>
    suspend fun setTemaOscuro(activo: Boolean)
    suspend fun setOrdenarPorPrioridad(activo: Boolean)
    suspend fun setOcultarCompletados(activo: Boolean)
}