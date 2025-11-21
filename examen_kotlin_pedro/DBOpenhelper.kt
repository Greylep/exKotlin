package com.example.examen_kotlin_pedro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen_kotlin_pedro.ProductosContract.Entrada

class DBOpenHelper private constructor(private val context: Context) :
    SQLiteOpenHelper(context, ProductosContract.NOMBRE_BD, null, ProductosContract.VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ${Entrada.TABLA} (${Entrada.IDCOL} INTEGER PRIMARY KEY, ${Entrada.NOMBRE_COL} TEXT, ${Entrada.CATEGORIA_COL} TEXT, ${Entrada.DESCRIPCION_COL} TEXT, ${Entrada.PRECIO_COL} REAL, ${Entrada.IMAGEN_COL} INTEGER)")
        db?.let { inicializarBBDD(it) }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Entrada.TABLA}")
        onCreate(db)
    }

    private fun inicializarBBDD(db: SQLiteDatabase) {
        val lista = ProductosProvider.cargarProductos(context)
        db.beginTransaction()
        try {
            for (p in lista) {
                val values = ContentValues().apply {
                    put(Entrada.NOMBRE_COL, p.nombre)
                    put(Entrada.CATEGORIA_COL, p.categoria)
                    put(Entrada.DESCRIPCION_COL, p.descripcion)
                    put(Entrada.PRECIO_COL, p.precio)
                    put(Entrada.IMAGEN_COL, p.imagenResId)
                }
                db.insert(Entrada.TABLA, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerProductos(): List<Productos> {
        val lista = mutableListOf<Productos>()
        val db = readableDatabase
        db.rawQuery("SELECT * FROM ${Entrada.TABLA}", null).use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    lista.add(Productos(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(Entrada.IDCOL)),
                        nombre = cursor.getString(cursor.getColumnIndexOrThrow(Entrada.NOMBRE_COL)),
                        categoria = cursor.getString(cursor.getColumnIndexOrThrow(Entrada.CATEGORIA_COL)),
                        descripcion = cursor.getString(cursor.getColumnIndexOrThrow(Entrada.DESCRIPCION_COL)),
                        precio = cursor.getDouble(cursor.getColumnIndexOrThrow(Entrada.PRECIO_COL)),
                        imagenResId = cursor.getInt(cursor.getColumnIndexOrThrow(Entrada.IMAGEN_COL))
                    ))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return lista
    }

    fun actualizarProducto(producto: Productos): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Entrada.NOMBRE_COL, producto.nombre)
            put(Entrada.CATEGORIA_COL, producto.categoria)
            put(Entrada.PRECIO_COL, producto.precio)
        }
        val affectedRows = db.update(Entrada.TABLA, values, "${Entrada.IDCOL} = ?", arrayOf(producto.id.toString()))
        db.close()
        return affectedRows
    }


    companion object {
        @Volatile
        private var instance: DBOpenHelper? = null
        fun getInstance(context: Context): DBOpenHelper {
            return instance ?: synchronized(this) {
                instance ?: DBOpenHelper(context.applicationContext).also { instance = it }
            }
        }
    }
}