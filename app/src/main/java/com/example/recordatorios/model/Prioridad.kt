package com.example.recordatorios.model

sealed class Prioridad(val etiqueta: String) {
    object Alta : Prioridad("Alta")
    object Media : Prioridad("Media")
    object Baja : Prioridad("Baja")
}