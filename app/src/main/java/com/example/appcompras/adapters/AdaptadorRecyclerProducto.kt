package com.example.appcompras.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.appcompras.R
import com.example.appcompras.models.Producto


class AdaptadorRecyclerProducto(var context: Context, var listaDatos: List<*>, var tipoHolder: Int, var onClickListenerCustom: OnClickListenerCustom) : RecyclerView.Adapter<ViewHolder>() {


    inner class ProductoHolder1(var view:View) : ViewHolder(view) {
        var nombre: TextView
        var precio: TextView
        var imagen: ImageView
        var boton: Button;

        init {
            nombre = view.findViewById(R.id.nombre_item_1)
            precio = view.findViewById(R.id.precio_item_1)
            imagen = view.findViewById(R.id.imagen_item_1)
            boton = view.findViewById(R.id.boton_carrito_item_1)
        }
    }

    inner class ProductoHolder2(var view:View) : ViewHolder(view) {
        var nombre: TextView
        var precio: TextView
        var imagen: ImageView

        init {
            nombre = view.findViewById(R.id.nombre_item_2)
            precio = view.findViewById(R.id.precio_item_2)
            imagen = view.findViewById(R.id.imagen_item_2)
        }
    }



    //MÉTODOS ADAPTER
    //- Crea viewholder y lo retorna
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (tipoHolder) {
            1 -> {
                var view: View =
                    LayoutInflater.from(context).inflate(R.layout.producto_holder1, parent, false)
                return ProductoHolder1(view)
            }

            2 -> {
                var view: View =
                    LayoutInflater.from(context).inflate(R.layout.producto_holder2, parent, false)
                return ProductoHolder2(view)
            }
            //opcion por defecto (es la misma que el tipo 2)
            else -> {
                var view: View =
                    LayoutInflater.from(context).inflate(R.layout.producto_holder2, parent, false)
                return ProductoHolder2(view)
            }
        }
    }

    //- conecta la parte visual del holder con los datos de cada item de la lista
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var producto: Producto = listaDatos.get(position) as Producto

        when(viewHolder) {
            is ProductoHolder1 -> {
                viewHolder.nombre.setText(producto.title)
                viewHolder.precio.setText("${producto.price} €")
                Glide.with(context).load(producto.thumbnail).into(viewHolder.imagen)
                viewHolder.boton.setOnClickListener() {
                    onClickListenerCustom.onButtonClick(producto)
                }
            }

            is ProductoHolder2 -> {
                viewHolder.nombre.setText(producto.title)
                viewHolder.precio.setText("${producto.price} €")
                Glide.with(context).load(producto.thumbnail).into(viewHolder.imagen)
            }
        }
    }

    //- cuenta el tamaño de la lista
    override fun getItemCount(): Int {
        return listaDatos.size
    }

    //INTERFAZ FUNCIONAL
    interface OnClickListenerCustom{
        fun onButtonClick(producto : Producto)
    }
}