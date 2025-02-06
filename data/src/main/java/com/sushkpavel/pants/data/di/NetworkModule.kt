package com.sushkpavel.pants.data.di

import com.sushkpavel.pants.data.remote.api.color.ColorApiService
import com.sushkpavel.pants.data.remote.api.color.factory.ColorApiFactory
import org.koin.dsl.module

val networkModule = module {
    single {
        ColorApiFactory()
    }
    single{
        get<ColorApiFactory>().build().create(ColorApiService::class.java) // to remove retrofit from app module
    }
}
