package com.example.testtask.ui.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.example.testtask.data.ProductsPagingSource
import com.example.testtask.data.Repository
import com.example.testtask.data.model.Product
import com.example.testtask.util.REQUEST_PAGE_SIZE
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val repository: Repository,
    app: Application
) : AndroidViewModel(app) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productsFlowable by lazy {
        Pager(
            config = PagingConfig(REQUEST_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ProductsPagingSource(repository) })
            .flowable
            .cachedIn(viewModelScope)
    }

    fun getProducts(): Flowable<PagingData<Product>>? {
        return if (repository.isApiInitialized()) productsFlowable else null
    }
}