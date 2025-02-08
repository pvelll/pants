package com.sushkpavel.pants.data

import com.sushkpavel.pants.data.remote.api.color.ColorApiService
import com.sushkpavel.pants.data.remote.repository.ColorRepositoryImpl
import com.suskpavel.pants.domain.model.ColorResponse
import com.suskpavel.pants.domain.model.Hsv
import com.suskpavel.pants.domain.model.Name
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class ColorRepositoryImplTest {

    private lateinit var apiService: ColorApiService
    private lateinit var colorRepository: ColorRepositoryImpl

    @BeforeEach
    fun setUp() {
        apiService = mock(ColorApiService::class.java)
        colorRepository = ColorRepositoryImpl(apiService)
    }

    @Test
    fun `should return specified number of random colors`() = runTest {
        // Arrange
        val count = 5
        val responses = generateColorResponses(count)

        `when`(apiService.getColor(anyString()))
            .thenAnswer { responses.removeFirst() }

        // Act
        val result = colorRepository.getRandomColors(count).getOrThrow()

        // Assert
        assertEquals(count, result.size)
    }

    @Test
    fun `should not contain duplicate colors`() = runTest {
        val count = 5
        val responses = generateColorResponses(count)

        `when`(apiService.getColor(anyString()))
            .thenAnswer { responses.removeFirstOrNull() ?: generateColorResponses(count) }

        val result = colorRepository.getRandomColors(count).getOrThrow()

        // Assert
        assertEquals(count, result.size)
        assertEquals(result.size, result.toSet().size)
    }


    @Test
    fun `should filter out colors with saturation lt 30 or value lt 40`() = runTest {
        val count = 5
        val responses = generateColorResponses(count)

        `when`(apiService.getColor(anyString()))
            .thenAnswer { responses.removeFirst() }

        val result = colorRepository.getRandomColors(count).getOrThrow()
        assertTrue(result.all {
            it.saturation >= 0.3f && it.value >= 0.4f
        })

    }

    @Test
    fun `should filter out colors with common names`() = runTest {
        val count = 5
        val responses = generateColorResponses(count)

        `when`(apiService.getColor(anyString()))
            .thenAnswer { responses.removeFirst() }

        val result = colorRepository.getRandomColors(count).getOrThrow()
        assertTrue(result.all {
            (it.name !in ColorRepositoryImpl.COMMON_USE_NAMES)
        })
    }

    private fun generateColorResponses(count: Int): LinkedList<ColorResponse> {
        val responses = LinkedList<ColorResponse>()
        for (i in 1..count) {
            responses.add(
                ColorResponse(
                    Name("Color$i"),
                    Hsv(Hsv.Fraction(0f, 0.5f, 0.5f))
                )
            )
        }
        return responses
    }
}