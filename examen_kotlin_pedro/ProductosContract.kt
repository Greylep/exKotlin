package com.example.examen_kotlin_pedro

import android.provider.BaseColumns

object ProductosContract {
    const val NOMBRE_BD = "productos.db"
    const val VERSION = 1

    object Entrada : BaseColumns {
        const val TABLA = "productos"
        const val IDCOL = "id"
        const val NOMBRE_COL = "nombre"
        const val CATEGORIA_COL = "categoria"
        const val DESCRIPCION_COL = "descripcion"
        const val PRECIO_COL = "precio"
        const val IMAGEN_COL = "imagen"
    }
}
