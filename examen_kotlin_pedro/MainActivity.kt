package com.example.examen_kotlin_pedro

import com.example.examen_kotlin_pedro.R

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.examen_kotlin_pedro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductosAdapter
    private lateinit var dbHelper: DBOpenHelper
    private lateinit var listaCompleta: List<Productos> // Tipo List, ya que no se modifica directamente aquí
    private lateinit var detalleLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 💡 Usamos ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 💡 Inicialización correcta del Singleton
        dbHelper = DBOpenHelper.getInstance(this)

        setupDetalleLauncher()
        initData()
        initRecyclerView()
        setupSwipeRefresh()

        // Registrar el RecyclerView para el menú contextual
        registerForContextMenu(binding.rvProductos)
    }

    private fun initData() {
        // Carga inicial y lista de respaldo
        listaCompleta = dbHelper.obtenerProductos()
    }

    private fun initRecyclerView() {
        binding.rvProductos.layoutManager = LinearLayoutManager(this)
        // 💡 El adapter debe inicializarse con una lista mutable si usas las funciones de filtrado,
        // pero la actualiza desde el método actualizarLista()
        adapter = ProductosAdapter(listaCompleta) { producto -> onItemSelected(producto) }
        binding.rvProductos.adapter = adapter
    }

    private fun setupDetalleLauncher() {
        detalleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                recargarDatos()
            }
        }
    }

    private fun onItemSelected(producto: Productos) {
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra("producto", producto)
        detalleLauncher.launch(intent)
    }



    override fun onContextItemSelected(item: MenuItem): Boolean {

        return true
    }



    // SWIPE REFRESH

    override fun onRefresh() {
        recargarDatos()
        binding.srlDatos.isRefreshing = false
    }

    private fun setupSwipeRefresh() {
        binding.srlDatos.setOnRefreshListener(this)
    }

    private fun recargarDatos() {
        // 💡 Vuelve a obtener la fuente de datos fresca de la BD
        listaCompleta = dbHelper.obtenerProductos()
        adapter.actualizarLista(listaCompleta)
        Toast.makeText(this, "Lista recargada (${listaCompleta.size})", Toast.LENGTH_SHORT).show()
    }
}