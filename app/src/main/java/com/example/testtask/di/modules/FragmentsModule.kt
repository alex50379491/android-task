package com.example.testtask.di.modules

import com.example.testtask.ui.product.ProductDetailsFragment
import com.example.testtask.ui.product.ProductsFragment
import com.example.testtask.ui.quotes.QuotesFragment
import com.example.testtask.ui.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun contributeQuotesFragment(): QuotesFragment

    @ContributesAndroidInjector
    abstract fun contributeProductsFragment(): ProductsFragment

    @ContributesAndroidInjector
    abstract fun contributeProductDetailsFragment(): ProductDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}