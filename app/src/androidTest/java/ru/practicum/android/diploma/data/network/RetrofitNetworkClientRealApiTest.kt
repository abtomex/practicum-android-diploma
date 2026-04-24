package ru.practicum.android.diploma.data.network

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto

@RunWith(AndroidJUnit4::class)
class RetrofitNetworkClientRealApiTest {

    private lateinit var networkClient: NetworkClient


    @Before
    fun setUp() {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_ACCESS_TOKEN))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(EndpointsApiService::class.java)

        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        networkClient = RetrofitNetworkClient(apiService, context)
    }

    @Test
    fun realGetAreasApiMethodTest() = runBlocking {

        val request = AreasRequestDto()
        val response = networkClient.doRequest(request)

        assertNotNull(response)
        assertTrue(
            "Response code should be 200, but was ${response.resultCode}",
            response.resultCode == 200
        )
        println("Areas API response code: ${response.resultCode}")
    }

    @Test
    fun realGetIndustriesApiMethodTest() = runBlocking {

        val request = IndustriesRequestDto()
        val response = networkClient.doRequest(request)

        assertNotNull(response)
        assertTrue(
            "Response code should be 200, but was ${response.resultCode}",
            response.resultCode == 200
        )
        assertNotNull(response.body)
        println("Industries API response code: ${response.resultCode}")
        println("Industries count: ${(response.body as? List<*>)?.size}")
    }

    @Test
    fun realGetVacanciesApiMethodTest() {
        runBlocking {
            val request = VacanciesRequestDto()
            val response = networkClient.doRequest(request)

            assertNotNull(response)
            assertTrue(
                "Response code should be 200, but was ${response.resultCode}",
                response.resultCode == 200
            )
            assertNotNull(response.body)
            println("Vacancies API response code: ${response.resultCode}")

            response.body?.let { body ->
                println("Total vacancies found: ${(body as? ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto)?.found}")
                println("Vacancies in response: ${(body as? ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto)?.items?.size}")
            }
        }

    }

    @Test
    fun realGetVacanciesWithFiltersApiMethodTest() = runBlocking {

        val request = VacanciesRequestDto(
            area = 1,
            text = "kotlin",
            salary = 200000,
        )

        val response = networkClient.doRequest(request)

        assertNotNull(response)
        assertTrue(
            "Response code should be 200, but was ${response.resultCode}",
            response.resultCode == 200
        )
        assertNotNull(response.body)
        println("Filtered Vacancies API response code: ${response.resultCode}")
    }

    @Test
    fun realGetVacancyDetailsApiMethodTest() {

        runBlocking {

            val request = VacancyDetailsRequestDto(id = "0000258d-fb45-3152-bfeb-250a4c547384")
            val response = networkClient.doRequest(request)

            assertNotNull(response)
            assertTrue(
                "Response code should be 200, but was ${response.resultCode}",
                response.resultCode == 200
            )
            assertNotNull(response.body)
            println("Vacancy Details API response code: ${response.resultCode}")

            response.body?.let { body ->
                val details = body as? ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto
                println("Vacancy name: ${details?.name}")
                println("Employer: ${details?.employer?.name}")
            }
        }

    }

    @Test
    fun realGetVacanciesEmptySearchApiMethodTest() {

        runBlocking {
            val request = VacanciesRequestDto(
                text = "sdkjfhskjdhfkjsdhfkjsdhf", // несуществующий запрос
                area = 1
            )

            val response = networkClient.doRequest(request)

            assertNotNull(response)
            assertTrue(
                "Response code should be 200, but was ${response.resultCode}",
                response.resultCode == 200
            )
            assertNotNull(response.body)

            response.body?.let { body ->
                val vacancies = body as? ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
                println("Empty search response code: ${response.resultCode}")
                println("Found vacancies: ${vacancies?.found}")
                assertTrue(vacancies?.found == 0 || vacancies?.items.isNullOrEmpty())
            }
        }

    }

}
