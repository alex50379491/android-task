package com.example.testtask.data

import android.content.SharedPreferences
import com.example.testtask.data.model.Products
import com.example.testtask.data.model.Quotes
import com.example.testtask.util.URL_SETTINGS
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiClient @Inject constructor(
    private val httpClient: OkHttpClient,
    private val gsonConverterFactory: GsonConverterFactory,
    private val rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
    private val urlSettingsPrefs: SharedPreferences
) {

    private var api: RequestApi?

    init {
        val url = getUrl()
        api = if (url != null) createApi(url) else null
    }

    fun setUrl(url: String?): Completable {
        return try {
            if (url.isNullOrBlank()) {
                api = null
                urlSettingsPrefs.edit().remove(URL_SETTINGS).apply()
            } else {
                api = createApi(url)
                urlSettingsPrefs.edit().putString(URL_SETTINGS, url).apply()
            }
            Completable.complete()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    fun getUrl(): String? {
        return urlSettingsPrefs.getString(URL_SETTINGS, null)
    }

    fun isUrlSet(): Boolean {
        return getUrl() != null
    }

    fun testUrl(url: String): Completable {
        return try {
            val testApi = createApi(url)
            testApi.getProducts(0, 1)
                .ignoreElement()
        } catch (e: Exception) {
            Completable.error(e)
        }
    }

    fun getQuotes(skip: Int, limit: Int): Single<Quotes> {
        return api?.getQuotes(skip, limit) ?: Single.error(Exception("No URL"))
    }

    fun getProducts(skip: Int, limit: Int): Single<Products> {
        return api?.getProducts(skip, limit) ?: Single.error(Exception("No URL"))
    }

    private fun createApi(url: String): RequestApi {
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava3CallAdapterFactory)
            .baseUrl(url)
            .build()

        return retrofit.create(RequestApi::class.java)
    }
}