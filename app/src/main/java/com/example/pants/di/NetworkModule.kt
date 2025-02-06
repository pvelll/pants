package com.example.pants.di

import com.sushkpavel.pants.data.remote.api.color.ColorApi
import com.sushkpavel.pants.data.remote.api.color.factory.ColorApiFactory
import org.koin.dsl.module

val networkModule = module {
    single {
        ColorApiFactory().build().create(ColorApi::class.java) // to remove retrofit from app module
    }
}
