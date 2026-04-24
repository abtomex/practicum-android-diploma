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

@RunWith(AndroidJUnit4::class)
class RetrofitNetworkClientRealApiTest {

    private lateinit var networkClient: NetworkClient


    @Before
    fun setUp() {
        // 1. Создаем OkHttpClient с интерцепторами
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(BuildConfig.API_ACCESS_TOKEN))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        // 2. Создаем Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 3. Создаем ApiService
        val apiService = retrofit.create(EndpointsApiService::class.java)

        // 4. Создаем NetworkClient с реальным контекстом
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        networkClient = RetrofitNetworkClient(apiService, context)
    }

    @Test
    fun realGetAreasApiMethodTest() = runBlocking {
        // Given
        val request = AreasRequestDto()

        // When
//        val response = networkClient.doRequest(request)
        val response = networkClient.doRequestList(request)

        // Then
        assertNotNull(response)
        assertTrue(
            "Response code should be 200-299, but was ${response.resultCode}",
            response.resultCode in 200..299
        )
        println("✅ Areas API response code: ${response.resultCode}")
    }

}

