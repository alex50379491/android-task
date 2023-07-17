package com.example.testtask.data

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.testtask.data.model.Quote
import com.example.testtask.data.model.Quotes
import com.example.testtask.util.REQUEST_PAGE_SIZE
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class QuotesPagingSource(private val repository: Repository) : RxPagingSource<Int, Quote>() {

    override fun getRefreshKey(state: PagingState<Int, Quote>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(REQUEST_PAGE_SIZE)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(REQUEST_PAGE_SIZE)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Quote>> {
        val skipCount = params.key ?: 0
        return repository.getQuotes(skipCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { toLoadResult(it, skipCount) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(quotes: Quotes, skip: Int): LoadResult<Int, Quote> {
        val prevKey = skip - REQUEST_PAGE_SIZE
        val nextKey = skip + REQUEST_PAGE_SIZE
        return LoadResult.Page(
            quotes.quotes,
            if (prevKey >= 0) prevKey else null,
            if (nextKey < quotes.total) nextKey else null
        )
    }
}