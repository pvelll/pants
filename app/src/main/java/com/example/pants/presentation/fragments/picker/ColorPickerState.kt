package com.example.pants.presentation.fragments.picker

import androidx.compose.ui.graphics.Color
import com.suskpavel.pants.domain.model.ColorModel

data class ColorPickerState(
    val colorBoard: List<ColorModel> = emptyList(),
    val currentColorName: String? = null,
    val selectedColor: Color = Color.Black
)