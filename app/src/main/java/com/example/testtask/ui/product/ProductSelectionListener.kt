package com.example.testtask.ui.product

import com.example.testtask.data.model.Product

fun interface ProductSelectionListener {

    fun onProductSelected(product: Product)
}