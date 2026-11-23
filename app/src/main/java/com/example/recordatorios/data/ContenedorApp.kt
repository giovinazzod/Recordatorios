package com.example.recordatorios.data

import android.content.Context
import com.example.recordatorios.data.preferencias.PreferenciasDataStore
import com.example.recordatorios.data.preferencias.RepositorioPreferencias

// Proveedor centralizado de dependencias para desacoplar la creación de instancias
object ContenedorApp {

    lateinit var preferencias: RepositorioPreferencias
        private set

    // Repositorio temporal en memoria (reemplazar luego por Room)
    val repositorio: RecordatoriosRepository by lazy {
        RecordatoriosEnMemoriaRepository()
    }

    fun inicializar(context: Context) {
        val appContext = context.applicationContext
        preferencias = PreferenciasDataStore(appContext)
    }
}