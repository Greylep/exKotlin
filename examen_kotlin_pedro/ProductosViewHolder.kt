package com.example.examen_kotlin_pedro

import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.examen_kotlin_pedro.databinding.ItemProductoBinding
import com.squareup.picasso.Picasso

class ProductosViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

    private val binding = ItemProductoBinding.bind(view)

    fun render(producto: Productos, onClickListener: (Productos) -> Unit) {
        binding.tvNombre.text = producto.nombre
        binding.tvCategoria.text = producto.categoria

        Picasso.get()
            .load(producto.imagenResId)
            .into(binding.ivProducto) //

        itemView.setOnClickListener { onClickListener(producto) }
        itemView.setOnCreateContextMenuListener(this)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu?.setHeaderTitle(binding.tvNombre.text)
        menu?.add(this.adapterPosition, 0, 0, "Eliminar")
        menu?.add(this.adapterPosition, 1, 1, "Editar")
    }
}