package com.sushkpavel.pants.data.remote.repository

import com.suskpavel.pants.domain.model.ColorModel
import com.sushkpavel.pants.data.remote.api.color.ColorApiService
import com.sushkpavel.pants.data.util.generateRandomColor
import com.sushkpavel.pants.data.util.toColorModel
import com.suskpavel.pants.domain.repository.ColorRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ColorRepositoryImpl(
    private val apiService: ColorApiService,
) : ColorRepository {

    override suspend fun getRandomColors(count: Int): Result<Set<ColorModel>> = runCatching {
        coroutineScope {
            val uniqueColors = mutableSetOf<ColorModel>()
            val commonNamesLower = COMMON_USE_NAMES
            while (uniqueColors.size < count) {
                val required = count - uniqueColors.size
                val colors = (1..required).map {
                    async { apiService.getColor(generateRandomColor()).toColorModel() }
                }.awaitAll()
                val validColors = colors.filter { color ->
                    color.name.lowercase() !in commonNamesLower && uniqueColors.add(color)
                }
                uniqueColors.addAll(validColors)
            }
            uniqueColors
        }
    }

  companion object {
        val COMMON_USE_NAMES = setOf(
            "beige",
            "black",
            "blue violet",
            "blue",
            "brown",
            "crimson",
            "cyan",
            "gold",
            "gray",
            "green",
            "indigo",
            "khaki",
            "lavender",
            "lime green",
            "magenta",
            "maroon",
            "navy blue",
            "olive",
            "orange",
            "pink",
            "plum",
            "purple",
            "red",
            "salmon",
            "silver",
            "sky blue",
            "teal",
            "violet",
            "white",
            "yellow",
        )
    }
}
