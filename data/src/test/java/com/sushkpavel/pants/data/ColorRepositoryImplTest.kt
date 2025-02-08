package com.sushkpavel.pants.data

import com.sushkpavel.pants.data.remote.api.color.ColorApiService
import com.sushkpavel.pants.data.remote.repository.ColorRepositoryImpl
import com.suskpavel.pants.domain.model.ColorResponse
import com.suskpavel.pants.domain.model.Hsv
import com.suskpavel.pants.domain.model.Name
import com.suskpavel.pants.domain.repository.ColorRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.whenever
import kotlin.system.measureTimeMillis


class ColorRepositoryImplTest {

    private lateinit var apiService: ColorApiService
    private lateinit var colorRepository: ColorRepositoryImpl

    @BeforeEach
    fun setUp() {
        apiService = mock(ColorApiService::class.java)
        colorRepository = ColorRepositoryImpl(apiService)
    }

    @Test
    fun `test getRandomColors returns unique colors`() = runTest {
        // Arrange
        val count = 5
        val responses = listOf(
            ColorResponse(Name("Color1"), Hsv(Hsv.Fraction(40f, 50f, 60f))),
            ColorResponse(Name("Color2"), Hsv(Hsv.Fraction(50f, 55f, 65f))),
            ColorResponse(Name("Color3"), Hsv(Hsv.Fraction(60f, 70f, 80f))),
            ColorResponse(Name("Color4"), Hsv(Hsv.Fraction(70f, 75f, 85f))),
            ColorResponse(Name("Color5"), Hsv(Hsv.Fraction(80f, 90f, 95f)))
        )

        `when`(apiService.getColor(anyString())).thenReturn(
            responses[0],
            responses[1],
            responses[2],
            responses[3],
            responses[4]
        )

        // Act
        val result = colorRepository.getRandomColors(count).getOrThrow()

        // Assert
        assertEquals(count, result.size)
    }

    @Test
    fun `test getRandomColors filters common names`() = runTest {
        // Arrange
        val count = 5
        val responses = listOf(
            ColorResponse(Name("blue"), Hsv(Hsv.Fraction(40f, 50f, 60f))),
            ColorResponse(Name("green"), Hsv(Hsv.Fraction(50f, 55f, 65f))),
            ColorResponse(Name("teal"), Hsv(Hsv.Fraction(60f, 70f, 80f))),
            ColorResponse(Name("Color4"), Hsv(Hsv.Fraction(70f, 75f, 85f))),
            ColorResponse(Name("Color5"), Hsv(Hsv.Fraction(80f, 90f, 95f)))
        )

        `when`(apiService.getColor(anyString())).thenReturn(
            responses[0],
            responses[1],
            responses[2],
            responses[3],
            responses[4]
        )

        // Act
        val result = colorRepository.getRandomColors(count).getOrThrow()

        // Assert
        assertEquals(2, result.size)
    }
}
