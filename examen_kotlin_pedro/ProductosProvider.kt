package com.example.examen_kotlin_pedro

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductosProvider {
    companion object {
        fun cargarProductos(context: Context): List<Productos> {
            val lista = mutableListOf<Productos>()
            try {
                val reader = BufferedReader(InputStreamReader(context.assets.open("productos.txt")))
                reader.forEachLine { linea ->
                    if (linea.isNotBlank()) {
                        val partes = linea.split("|")
                        if (partes.size >= 6) {
                            val id = partes[0].trim().toIntOrNull() ?: 0
                            val nombre = partes[1].trim()
                            val categoria = partes[2].trim()
                            val descripcion = partes[3].trim()
                            val precio = partes[4].trim().replace(",", ".").toDoubleOrNull() ?: 0.0
                            val imgName = partes[5].trim()
                            val imgId = context.resources.getIdentifier(imgName, "drawable", context.packageName)

                            // Si no encuentra la imagen, usa una por defecto para evitar crash
                            val finalImgId = if (imgId != 0) imgId else R.drawable.ic_launcher_foreground

                            lista.add(Productos(id, nombre, categoria, descripcion, precio, finalImgId))
                        }
                    }
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return lista
        }
    }
}
