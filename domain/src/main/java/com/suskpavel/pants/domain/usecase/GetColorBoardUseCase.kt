package com.suskpavel.pants.domain.usecase

import com.suskpavel.pants.domain.model.ColorModel
import com.suskpavel.pants.domain.repository.ColorRepository

class GetColorBoardUseCase(
    private val colorRepository: ColorRepository,
) {

    suspend operator fun invoke(colorCount: Int): Result<Set<ColorModel>> =
        colorRepository.getRandomColors(colorCount)
}
