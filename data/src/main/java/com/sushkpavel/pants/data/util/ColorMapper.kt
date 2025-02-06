package com.sushkpavel.pants.data.util

import com.suskpavel.pants.domain.model.ColorResponse
import com.suskpavel.pants.domain.model.ColorModel

fun ColorResponse.toColorModel(): ColorModel {
    return ColorModel(
        name = this.name.value,
        realHue = this.hsv.fraction.h,
        guessHue = null,
        saturation = this.hsv.fraction.s,
        value = this.hsv.fraction.v,
    )
}
