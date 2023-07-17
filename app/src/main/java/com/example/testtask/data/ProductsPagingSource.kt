package com.example.testtask.data

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.testtask.data.model.Product
import com.example.testtask.data.model.Products
import com.example.testtask.util.REQUEST_PAGE_SIZE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class ProductsPagingSource(private val repository: Repository) : RxPagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(REQUEST_PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(REQUEST_PAGE_SIZE)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Product>> {
        val skipCount = params.key ?: 0
        return repository.getProducts(skipCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { toLoadResult(it, skipCount) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(products: Products, skip: Int): LoadResult<Int, Product> {
        val prevKey = skip - REQUEST_PAGE_SIZE
        val nextKey = skip + REQUEST_PAGE_SIZE
        return LoadResult.Page(
            products.products,
            if (prevKey >= 0) prevKey else null,
            if (nextKey < products.total) nextKey else null
        )
    }
}