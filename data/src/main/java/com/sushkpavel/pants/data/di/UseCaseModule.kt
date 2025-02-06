package com.sushkpavel.pants.data.di

import com.suskpavel.pants.domain.usecase.CheckBoardOrderUseCase
import com.suskpavel.pants.domain.usecase.GetColorBoardUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetColorBoardUseCase)
    factoryOf(::CheckBoardOrderUseCase)
}
