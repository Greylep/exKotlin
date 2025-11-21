

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.examen_kotlin_pedro.databinding.ActivityDetalleBinding
import com.squareup.picasso.Picasso

class DetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleBinding
    private lateinit var dbHelper: DBOpenHelper
    private lateinit var productoOriginal: Productos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBOpenHelper.getInstance(this)!!

        @Suppress("DEPRECATION")
        val producto = intent.getSerializableExtra("producto") as Productos?

        if (producto == null) {
            finish()
            return
        }
        productoOriginal = producto

        cargarDatos(productoOriginal)

        binding.btnGuardar.setOnClickListener {
            guardarCambios()
        }
        binding.btnVolver.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun cargarDatos(producto: Productos) {
        title = "Detalle y Edición"

        Picasso.get().load(producto.imagenResId).into(binding.ivProducto)

        binding.etNombre.setText(producto.nombre)
        binding.etCategoria.setText(producto.categoria)
        binding.etPrecio.setText(producto.precio.toString())

        binding.tvDescripcion.text = "Descripción: ${producto.descripcion}"
    }

    private fun guardarCambios() {
        val nuevoNombre = binding.etNombre.text.toString().trim()
        val nuevaCategoria = binding.etCategoria.text.toString().trim()
        val nuevoPrecio = binding.etPrecio.text.toString().toDoubleOrNull()

        if (nuevoNombre.isEmpty() || nuevaCategoria.isEmpty() || nuevoPrecio == null) {
            Toast.makeText(this, "Campos inválidos.", Toast.LENGTH_SHORT).show()
            return
        }

        val productoModificado = productoOriginal.copy(
            nombre = nuevoNombre,
            categoria = nuevaCategoria,
            precio = nuevoPrecio,
            descripcion = productoOriginal.descripcion
        )

        dbHelper.actualizarProducto(productoModificado)

        setResult(Activity.RESULT_OK)
        finish()
    }
}