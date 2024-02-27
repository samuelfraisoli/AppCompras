package com.example.appcompras.models

import java.io.Serializable

data class Producto(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: ArrayList<String>
    ) : Serializable
