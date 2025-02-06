package com.sushkpavel.pants.data.remote.repository

import com.suskpavel.pants.domain.model.ColorModel
import com.sushkpavel.pants.data.remote.api.color.ColorApi
import com.sushkpavel.pants.data.util.generateRandomColor
import com.sushkpavel.pants.data.util.toColorModel
import com.suskpavel.pants.domain.repository.ColorRepository
import java.util.Locale

class ColorRepositoryImpl(
    private val apiService: ColorApi,
) : ColorRepository {

    override suspend fun getRandomColors(count: Int): Result<Set<ColorModel>> = runCatching {
        val colorList = mutableListOf<ColorModel>()

        while (colorList.size < count) {
            val color = apiService.getColor(generateRandomColor()).toColorModel()
            val notContainCommon = color.name.lowercase(Locale.getDefault()) !in COMMON_USE_NAMES
            val isDistinct = color !in colorList
            if (notContainCommon && isDistinct) {
                colorList.add(color)
            }
        }
        colorList.toSet()
    }

    private companion object {
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
