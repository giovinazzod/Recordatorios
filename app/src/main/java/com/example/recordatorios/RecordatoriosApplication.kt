package com.example.recordatorios

import android.app.Application
import com.example.recordatorios.data.ContenedorApp

// Clase de aplicación que inicializa las dependencias antes de que se cree cualquier pantalla
class RecordatoriosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ContenedorApp.inicializar(this)
    }
}