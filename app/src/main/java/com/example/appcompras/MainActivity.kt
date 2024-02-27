package com.example.appcompras

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.appcompras.adapters.AdaptadorRecyclerProducto
import com.example.appcompras.databinding.ActivityMainBinding
import com.example.appcompras.models.Producto
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), AdaptadorRecyclerProducto.OnClickListenerCustom {
    private lateinit var binding: ActivityMainBinding

    //volley queue de requests
    private lateinit var volleyQueue : RequestQueue

    //spinner
    private var arrayCategorias: ArrayList<String> = ArrayList()
    private lateinit var adapterSpinner: ArrayAdapter<String>

    //recycler
    private var arrayProductos: ArrayList<Producto> = ArrayList()
    private var arrayProductosFiltrado: ArrayList<Producto> = ArrayList()
    private lateinit var adaptadorRecycler: AdaptadorRecyclerProducto

    //arraylist carrito
    private var arrayCarrito: ArrayList<Producto> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        volleyQueue = Volley.newRequestQueue(this)
        crearToolbar()
        inicializarAdaptadorRecycler()
        inicializarRecycler()
        crearAdapterSpinner()
        añadirListenerSpinner()

        inicializarArrayCategorias()
        inicializarArrayProductos()
        recogerDatosIntent()
    }


    //TOOLBAR --------------------------------------------------------------------------------------
    fun crearToolbar() {
        setSupportActionBar(binding.toolbarMain)
    }

    //menú toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_pincipal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.carrito -> {
                ejecutarIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    //SPINNER --------------------------------------------------------------------------------------
    fun crearAdapterSpinner() {
        adapterSpinner = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayCategorias)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.spinnerCategorias.setAdapter(adapterSpinner)
    }

    fun añadirListenerSpinner() {
        binding.spinnerCategorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var opcionElegidaSpinner  = binding.spinnerCategorias.getItemAtPosition(p2).toString()
                Log.v("", "$opcionElegidaSpinner")
                reiniciarProductosFiltrados(opcionElegidaSpinner)
                adaptadorRecycler.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }





    //RECYCLERVIEW ---------------------------------------------------------------------------------
    private fun inicializarAdaptadorRecycler() {
        adaptadorRecycler = AdaptadorRecyclerProducto(this, arrayProductosFiltrado, 1, this)
    }

    private fun inicializarRecycler() {
        binding.recyclerProductos.adapter = adaptadorRecycler
        binding.recyclerProductos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    //rellenar metodo interfaz
    override fun onButtonClick(producto : Producto) {
        Toast.makeText(this, "Añadido ${producto.title} carrito ", Toast.LENGTH_SHORT).show()
        añadirAlCarrito(producto)
    }

    fun añadirAlCarrito(producto : Producto) {
        arrayCarrito.add(producto)
    }


    //UTILIDADES -----------------------------------------------------------------------------------

    //ENVIAR INTENT
    fun ejecutarIntent() {
        val intent: Intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("arrayCarrito", arrayCarrito)
        startActivity(intent)
    }

    //RECIBIR INTENT
    // - revisa si existe un intent antes de intentar acceder a el, si no, si no hay intent, se lanzaría un nullpointerexception
    fun recogerDatosIntent() {
        val extras = intent?.extras
        if (extras != null) {
            arrayCarrito = extras.getSerializable("arrayCarrito") as ArrayList<Producto>
        }
    }

    // REQUESTQUEUE
    fun crearYLanzarRequest(url: String, nombre: String, callback: (String) -> Unit) {
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                Log.v("", "recibido correctamente")
                // ejecuta la función callback, que pide un String y devuelve un unit
                callback(response.toString())

            },
            Response.ErrorListener { error ->
                Log.v("", "los datos no se han recibido")
                // Aquí puedes agregar alguna lógica adicional para manejar errores, si es necesario
            })

        // le da el nombre personalizado a la request (en Volley si metes dos request con el mismo nombre, se sobreescriben)
        stringRequest.tag = nombre
        // añade la request a la requestqueue
        volleyQueue.add(stringRequest)
    }


    //ARRAY CATEGORIAS
    fun inicializarArrayCategorias() {
        crearYLanzarRequest("https://dummyjson.com/products/categories", "stringRequest1") { response -> procesarJsonCategorias(response) }
    }

    fun procesarJsonCategorias(json : String) {
        var jsonArray = JSONArray(json)
        for(i in 0 until jsonArray.length()) {
            var categoria = jsonArray.getString(i)
            arrayCategorias.add(categoria)
        }
        adapterSpinner.notifyDataSetChanged()
    }


    //ARRAY PRODUCTOS
    fun inicializarArrayProductos() {
        crearYLanzarRequest("https://dummyjson.com/products", "stringRequest2") { response -> procesarJsonProductos(response) }

    }

    //ARRAY PRODUCTOS FILTRADOS
    fun reiniciarProductosFiltrados(opcionElegidaSpinner : String) {
        arrayProductosFiltrado.clear()

        for(producto in arrayProductos) {
            if(producto.category.equals(opcionElegidaSpinner, true)) {
                arrayProductosFiltrado.add(producto)
            }
        }
    }

    fun procesarJsonProductos(json : String) {
        var jsonObject1 = JSONObject(json)
        var jsonArray1 = jsonObject1.getJSONArray("products")
        for(i in 0 until jsonArray1.length()) {
            var jsonObject2 = jsonArray1.get(i) as JSONObject
            var id = jsonObject2.getLong("id")
            var title = jsonObject2.getString("title")
            var description = jsonObject2.getString("description")
            var price = jsonObject2.getDouble("price")
            var discountPercetage = jsonObject2.getDouble("discountPercentage")
            var rating = jsonObject2.getDouble("rating")
            var stock = jsonObject2.getInt("stock")
            var brand = jsonObject2.getString("brand")
            var category = jsonObject2.getString("category")
            var thumbnail = jsonObject2.getString("thumbnail")

            var images : java.util.ArrayList<String> = ArrayList()
            var jsonArrayImages = jsonObject2.getJSONArray("images")
            for(j in 0 until jsonArrayImages.length()) {
                var image = jsonArrayImages.getString(j)
                images.add(image)
            }

            arrayProductos.add(Producto(id, title, description, price, discountPercetage, rating, stock, brand, category, thumbnail, images))
        }

        //actualiza los datos del recycler una vez que se han cogido todos los productos. Se tiene que llamar aqui porque la funcion de volley se realiza de forma asíncrona
        adaptadorRecycler.notifyDataSetChanged()

    }

}


