package com.example.recordatorios.model

sealed class EstadoRecordatorio(val etiqueta: String) {
    object Pendiente : EstadoRecordatorio("Pendiente")
    object Completado : EstadoRecordatorio("Completado")
}