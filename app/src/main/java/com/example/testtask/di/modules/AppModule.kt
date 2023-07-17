package com.example.testtask.di.modules

import dagger.Module

@Module(
    includes = [
        NetworkModule::class,
        DataModule::class,
        ViewModelModule::class
    ]
)
class AppModule