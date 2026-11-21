package com.example.recordatorios.model

data class Categoria(
    val id: Int,
    val nombre: String
) {
    companion object {
        val Estudio = Categoria(1, "Estudio")
        val Trabajo = Categoria(2, "Trabajo")
        val Personal = Categoria(3, "Personal")

        val todas: List<Categoria> = listOf(Estudio, Trabajo, Personal)
    }
}
