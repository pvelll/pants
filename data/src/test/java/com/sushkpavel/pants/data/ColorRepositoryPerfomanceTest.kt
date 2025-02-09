import com.sushkpavel.pants.data.remote.api.color.ColorApiService
import com.sushkpavel.pants.data.remote.repository.ColorRepositoryImpl
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.system.measureTimeMillis

class ColorRepositoryImplPerformanceTest {

    private lateinit var apiService: ColorApiService
    private lateinit var colorRepository: ColorRepositoryImpl

    @BeforeEach
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.thecolorapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ColorApiService::class.java)
        colorRepository = ColorRepositoryImpl(apiService)
    }

    @Test
    fun `performance test for getRandomColors`() = runTest {
        val count = 100

        val timeElapsed = measureTimeMillis {
            runBlocking {
                val result = colorRepository.getRandomColors(count).getOrThrow()
                assertEquals(count, result.size)
            }
        }

        println("Time taken to fetch $count colors: $timeElapsed ms")
        assertTrue(timeElapsed < 10000)
    }
}
