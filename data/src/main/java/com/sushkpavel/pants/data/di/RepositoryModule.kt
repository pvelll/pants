package com.sushkpavel.pants.data.di

import com.sushkpavel.pants.data.remote.repository.ColorRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val productRepositoryModule = module {
    singleOf(::ColorRepositoryImpl) { bind<com.suskpavel.pants.domain.repository.ColorRepository>() }
}
