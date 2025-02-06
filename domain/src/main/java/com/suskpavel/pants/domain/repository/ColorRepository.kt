package com.suskpavel.pants.domain.repository

import com.suskpavel.pants.domain.model.ColorModel

interface ColorRepository {

    suspend fun getRandomColors(count: Int): Result<Set<ColorModel>>
}
