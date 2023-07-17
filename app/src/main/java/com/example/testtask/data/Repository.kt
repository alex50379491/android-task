package com.example.testtask.data

import com.example.testtask.data.model.Products
import com.example.testtask.data.model.Quotes
import com.example.testtask.util.REQUEST_PAGE_SIZE
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class Repository @Inject constructor(private val apiClient: ApiClient) {

    fun getQuotes(skip: Int): Single<Quotes> {
        return apiClient.getQuotes(skip, REQUEST_PAGE_SIZE)
    }

    fun getProducts(skip: Int): Single<Products> {
        return apiClient.getProducts(skip, REQUEST_PAGE_SIZE)
    }

    fun isApiInitialized() : Boolean {
        return apiClient.isUrlSet()
    }
}