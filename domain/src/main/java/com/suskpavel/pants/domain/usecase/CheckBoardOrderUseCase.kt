package com.suskpavel.pants.domain.usecase

import com.suskpavel.pants.domain.model.ColorModel

class CheckBoardOrderUseCase {

    operator fun invoke(board: List<ColorModel>): Boolean {
        return board.zipWithNext { a, b -> a.realHue <= b.realHue }.all { it }
    }
}
