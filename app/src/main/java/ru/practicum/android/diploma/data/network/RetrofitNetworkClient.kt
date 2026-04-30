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
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto
import ru.practicum.android.diploma.data.exceptions.UnexpectedRequestDtoException

class RetrofitNetworkClient(
    private val endpointsApiService: EndpointsApiService,
    private val context: Context

) : NetworkClient {

    override suspend fun doRequest(dto: Request): Response<out Any> {
        if (!isConnected()) {
            return Response.ErrorResponse(errorCode = Response.STATUS_NETWORK_ERROR)
        }
        return try {
            when (dto) {
                is AreasRequestDto -> processRequest(
                    call = { endpointsApiService.areas() },
                    onSuccess = { body -> Response.AreasResponse(body) }
                )

                is IndustriesRequestDto -> processRequest(
                    call = { endpointsApiService.industries() },
                    onSuccess = { body -> Response.IndustriesResponse(body) }
                )

                is VacanciesRequestDto -> processRequest(
                    call = { endpointsApiService.vacancies(dto.toQueryMap()) },
                    onSuccess = { body -> Response.VacanciesResponse(body) }
                )

                is VacancyDetailsRequestDto -> processRequest(
                    call = { endpointsApiService.vacancyDetails(dto.id) },
                    onSuccess = { body -> Response.VacancyDetailsResponse(body) }
                )

                else -> throw UnexpectedRequestDtoException("unexpected request dto")
            }
        } catch (_: Exception) {
            Response.ErrorResponse(Response.STATUS_SERVER_ERROR)
        }

    }

    private suspend fun <T> processRequest(
        call: suspend () -> Call<T>,
        onSuccess: (T) -> Response<out Any>
    ): Response<out Any> {
        return try {
            val response = call().execute()

            if (response.isSuccessful) {
                response.body()?.let { body ->
                    onSuccess(body)
                } ?: Response.ErrorResponse(response.code())
            } else {
                Response.ErrorResponse(response.code())
            }
        } catch (_: Exception) {
            Response.ErrorResponse(Response.STATUS_SERVER_ERROR)
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
        return false
    }

}
