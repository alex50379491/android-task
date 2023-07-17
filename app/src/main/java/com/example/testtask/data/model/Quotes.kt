package com.example.testtask.data.model

data class Quotes(
    val quotes: List<Quote>,
    val total: Int,
    val skip: Int,
    val limit: Int
)