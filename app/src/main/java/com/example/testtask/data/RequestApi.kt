package com.example.testtask.data

import com.example.testtask.data.model.Products
import com.example.testtask.data.model.Quotes
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestApi {

    @GET("/quotes")
    fun getQuotes(@Query("skip") skip: Int, @Query("limit") limit: Int): Single<Quotes>

    @GET("/products")
    fun getProducts(@Query("skip") skip: Int, @Query("limit") limit: Int): Single<Products>
}