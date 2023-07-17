package com.example.testtask.util

sealed class Result<T> {

    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val error: Throwable) : Result<T>()
    class Loading<T> : Result<T>()
}