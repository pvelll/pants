package com.sushkpavel.pants.data.di

import com.sushkpavel.pants.data.remote.networkModule
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule, productRepositoryModule, useCaseModule)
}
