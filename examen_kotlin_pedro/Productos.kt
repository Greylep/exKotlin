package com.example.examen_kotlin_pedro

import java.io.Serializable

data class Productos(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Double,
    val imagenResId: Int
) : Serializable
