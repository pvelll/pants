package com.example.pants.presentation.fragments

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pants.presentation.fragments.picker.ColorPickerState
import com.suskpavel.pants.domain.model.ColorModel
import com.suskpavel.pants.domain.usecase.CheckBoardOrderUseCase
import com.suskpavel.pants.domain.usecase.GetColorBoardUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedGameViewModel(
    private val getColorBoardUseCase: GetColorBoardUseCase,
    private val checkBoardOrderUseCase: CheckBoardOrderUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(ColorPickerState())
    val screenState: StateFlow<ColorPickerState> get() = _screenState.asStateFlow()
//    private val _colorBoard = MutableStateFlow(EMPTY_BOARD)
//    val colorBoard: StateFlow<List<ColorModel>> = _colorBoard.asStateFlow()
//
//    private val _currentColorName = MutableStateFlow<String?>(null)
//    val currentColorName: StateFlow<String?> = _currentColorName.asStateFlow()
//
//    private val _selectedColor = MutableStateFlow(Color.Black)
//    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    init {
        initColorBoard()
    }

    fun setColorModelByName(name: String) {
        val colorModel = _screenState.value.colorBoard.find { it.name == name }
        colorModel?.let {
            _screenState.update { currentState ->
                currentState.copy(
                    currentColorName = it.name,
                    selectedColor = Color.hsv(it.guessHue ?: 0f, 1f, 1f)
                )
            }
        }
    }

    fun saveColor(newHue: Float) {
        viewModelScope.launch {
            if (_screenState.value.colorBoard.isEmpty()) return@launch
            val updatedColors = _screenState.value.colorBoard.map {
                if (it.name == _screenState.value.currentColorName) it.updateHue(newHue) else it
            }
            _screenState.update { currentState ->
                currentState.copy(colorBoard = updatedColors)
            }
        }
    }

    fun updateColorSettings(hue: Float) {
        _screenState.update { currentState ->
            val newSelectedColor = Color.hsv(hue, 1f, 1f)
            val updatedColorBoard = currentState.colorBoard.map { colorModel ->
                if (Color.hsv(
                        colorModel.guessHue ?: 0f,
                        colorModel.saturation,
                        colorModel.value
                    ) != currentState.selectedColor
                ) {
                    colorModel.updateHue(colorModel.guessHue)
                } else {
                    colorModel.updateHue(hue)
                }
//                if (colorModel.name == currentState.currentColorName) {
//                    colorModel.updateHue(hue)
//                } else {
//                    colorModel
//                }
            }
            currentState.copy(
                selectedColor = newSelectedColor,
                colorBoard = updatedColorBoard
            )
        }
    }


//    fun updateColorSettings(hue: Float) {
//        _selectedColor.value = Color.hsv(hue, 1f, 1f)
//        Log.e("debug", "bonjour")
//        _colorBoard.value = _colorBoard.value.map { color ->
//            if (Color.hsv(
//                    color.guessHue ?: 0f,
//                    color.saturation,
//                    color.value
//                ) != _selectedColor.value
//            ) {
//                color.updateHue(color.guessHue)
//            } else {
//                color.updateHue(hue)
//            }
//        }
//    }

    fun checkColorOrder(board: List<ColorModel>): List<ColorModel>? {
        return when {
            board.isEmpty() -> {
                initColorBoard()
                board
            }

            checkBoardOrderUseCase(board) -> {
                initColorBoard()
                null
            }

            else -> {
                board.sortedBy { it.realHue }
            }
        }
    }

    private fun initColorBoard() {
        viewModelScope.launch {
            getColorBoardUseCase(BOARD_SIZE).fold(
                onSuccess = { colors ->
                    _screenState.update { currentState ->
                        currentState.copy(colorBoard = colors.toPersistentList())
                    }
                },
                onFailure = { error ->
                    _errorMessage.emit(error.message.orEmpty())
                }
            )
        }
    }

    private companion object {
        const val BOARD_SIZE = 5
    }
}