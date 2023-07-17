package com.example.testtask.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.testtask.data.ApiClient
import com.example.testtask.data.Repository
import com.example.testtask.util.URL_SETTINGS
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideApiClient(
        httpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory,
        urlSettingsPrefs: SharedPreferences
    ): ApiClient =
        ApiClient(httpClient, gsonConverterFactory, rxJava3CallAdapterFactory, urlSettingsPrefs)

    @Singleton
    @Provides
    fun provideUrlSettingsPrefs(app: Application): SharedPreferences {
        return app.getSharedPreferences(URL_SETTINGS, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideRepository(apiClient: ApiClient): Repository = Repository(apiClient)
}