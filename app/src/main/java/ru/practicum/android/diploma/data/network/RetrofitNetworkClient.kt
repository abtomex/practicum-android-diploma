package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import retrofit2.Call
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancy_details.VacancyDetailsRequestDto
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val endpointsApiService: EndpointsApiService,
    private val context: Context

) : NetworkClient {

    override suspend fun doRequest(dto: Request): Response {

        if (!isConnected()) {
            return Response().apply { resultCode = STATUS_NETWORK_ERROR }
        }

        when (dto) {
            is AreasRequestDto -> {
                return executeCall(endpointsApiService.areas())
            }
            is IndustriesRequestDto -> {
                return executeCall(endpointsApiService.industries())

            }
            is VacanciesRequestDto -> {
                return executeCall(endpointsApiService.vacancies(dto.toQueryMap()))

            }
            is VacancyDetailsRequestDto -> {
                return executeCall(endpointsApiService.vacancyDetails(dto.toQueryMap()))
            }
            else -> return Response()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: Response> executeCall (call : Call<T>) : T {
        return try {

            val resp = call.execute()
            val body = resp.body() ?: Response() as T
            return body.apply { resultCode = resp.code() }

        } catch (_: UnknownHostException) {
            createErrorResponse(STATUS_NO_INTERNET)
        } catch (_: SocketTimeoutException) {
            createErrorResponse(STATUS_TIMEOUT_ERROR)
        } catch (_: IOException) {
            createErrorResponse(STATUS_NETWORK_ERROR)
        } catch (_: Exception) {
            createErrorResponse(STATUS_UNKNOWN_ERROR)
        }

    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Response> createErrorResponse(code: Int): T {
        return Response().apply {
            resultCode = code
        } as T
    }
    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        private const val STATUS_NETWORK_ERROR = -1
        private const val STATUS_TIMEOUT_ERROR = -2
        private const val STATUS_UNKNOWN_ERROR = -3
        private const val STATUS_NO_INTERNET = -4
    }
}
