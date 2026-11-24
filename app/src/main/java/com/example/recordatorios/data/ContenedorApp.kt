package com.example.recordatorios.data

import android.content.Context
import com.example.recordatorios.data.local.AppDatabase
import com.example.recordatorios.data.local.crearBase
import com.example.recordatorios.data.preferencias.PreferenciasDataStore
import com.example.recordatorios.data.preferencias.RepositorioPreferencias

// Proveedor centralizado de dependencias para desacoplar la creación de instancias
object ContenedorApp {

    private lateinit var baseDeDatos: AppDatabase

    lateinit var preferencias: RepositorioPreferencias
        private set

    lateinit var repositorio: RecordatoriosRepository
        private set

    fun inicializar(context: Context) {
        val appContext = context.applicationContext
        baseDeDatos = crearBase(appContext)
        preferencias = PreferenciasDataStore(appContext)
        repositorio = RepositorioRoom(baseDeDatos.recordatorioDao())
    }
}