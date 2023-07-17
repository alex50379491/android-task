package com.example.testtask.data.model

import java.io.Serializable
import java.math.BigDecimal

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val discountPercentage: BigDecimal,
    val rating: BigDecimal,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>?
) : Serializable
