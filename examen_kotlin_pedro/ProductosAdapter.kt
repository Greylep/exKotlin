package com.example.examen_kotlin_pedro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductosAdapter(
    private var listaProductos: List<Productos>,
    private val onClick: (Productos) -> Unit
) : RecyclerView.Adapter<ProductosAdapter.ProductosViewHolder>() {

    inner class ProductosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.tvNombreProducto)
        private val precio: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        private val imagen: ImageView = itemView.findViewById(R.id.ivImagenProducto)

        fun bind(producto: Productos) {
            nombre.text = producto.nombre
            precio.text = "${producto.precio} €"
            imagen.setImageResource(producto.imagenResId)
            itemView.setOnClickListener { onClick(producto) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductosViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductosViewHolder, position: Int) {
        holder.bind(listaProductos[position])
    }

    override fun getItemCount(): Int = listaProductos.size

    // Función clave para que el filtro y el refresco funcionen
    fun actualizarLista(nuevaLista: List<Productos>) {
        listaProductos = nuevaLista
        notifyDataSetChanged() // Notifica al RecyclerView que debe redibujarse
    }
}
