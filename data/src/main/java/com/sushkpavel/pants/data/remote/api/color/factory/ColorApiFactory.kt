package com.sushkpavel.pants.data.remote.api.color.factory

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ColorApiFactory {
    fun build() : Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.thecolorapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}