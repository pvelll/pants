package com.sushkpavel.pants.data.remote.api.color


import com.suskpavel.pants.domain.model.ColorResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ColorApiService {
    @GET("id")
    suspend fun getColor(
        @Query("hsv") hsv: String
    ): ColorResponse
}
