package com.example.appcompras

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcompras.adapters.AdaptadorRecyclerProducto
import com.example.appcompras.databinding.ActivitySecondBinding
import com.example.appcompras.models.Producto
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity(), AdaptadorRecyclerProducto.OnClickListenerCustom {

    private lateinit var binding : ActivitySecondBinding

    //recycler
    private var arrayCarrito: ArrayList<Producto> = ArrayList()
    private lateinit var adaptadorRecycler : AdaptadorRecyclerProducto


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivitySecondBinding.inflate(layoutInflater)
            setContentView(binding.root)

        recogerDatosIntent()
        crearToolbar()
        inicializarAdaptadorRecycler()
        inicializarRecycler()
        mostrarPrecioTotal()
    }


    //TOOLBAR
    fun crearToolbar() {
        setSupportActionBar(binding.toolbarSecond)
    }


    //MENÚ TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_second, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirmar_compra -> {
                mostrarSnackbarCompra()
                true
            }
            R.id.vaciar_carrito -> {
                vaciarCarrito()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //RECYCLERVIEW
    fun inicializarAdaptadorRecycler() {
        adaptadorRecycler = AdaptadorRecyclerProducto(this, arrayCarrito, 2, this)

    }

    fun inicializarRecycler() {
        binding.recyclerCarrito.adapter = adaptadorRecycler
        binding.recyclerCarrito.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }


    //TEXTVIEW
    fun mostrarPrecioTotal() {
        var precioTotal = 0.0;
        for(producto in arrayCarrito) {
            precioTotal += producto.price
        }
        binding.textviewPrecioTotal.text = "$precioTotal €"
    }

    override fun onButtonClick(producto: Producto) {
    }

    //UTILIDAD -------------------------------------------------------------------------------------

    fun vaciarCarrito() {
        arrayCarrito.clear()
        adaptadorRecycler.notifyDataSetChanged()
        binding.textviewPrecioTotal.text = "0.0 €"
        mostrarSnackbarVaciarCarrito()
    }

    fun mostrarSnackbarVaciarCarrito() {
        Snackbar.make(binding.root, "Carrito vaciado", Snackbar.LENGTH_SHORT).show()
    }

    fun mostrarSnackbarCompra() {
        Snackbar.make(binding.root, "Enhorabuena, compra por valor de ${binding.textviewPrecioTotal.text} € realizada.", Snackbar.LENGTH_SHORT).show()

    }

    //ENVIAR INTENT (BOTÓN BACK)
    override fun onBackPressed() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.putExtra("arrayCarrito", arrayCarrito)
        startActivity(intent)
        super.onBackPressed()
    }

    //RECIBIR INTENT
    fun recogerDatosIntent() {
        arrayCarrito = intent.extras?.getSerializable("arrayCarrito") as ArrayList<Producto>
    }
}