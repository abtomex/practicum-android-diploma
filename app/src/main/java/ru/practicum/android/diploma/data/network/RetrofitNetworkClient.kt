package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto

class RetrofitNetworkClient(
    private val endpointsApiService: EndpointsApiService,
    private val context: Context

) : NetworkClient {

    override suspend fun doRequest(dto: Request): Response<out Any> {
        if (!isConnected()) {
            return Response.ErrorResponse( errorCode = Response.STATUS_NETWORK_ERROR )
        }

        when (dto) {
            is AreasRequestDto -> {
                val resp = endpointsApiService.areas().execute()
                val body = resp.body()
                if (body != null) {
                    return Response.AreasResponse(body)
                }
            }
            is IndustriesRequestDto -> {
                val resp = endpointsApiService.industries().execute()
                val body = resp.body()
                if (body != null) {
                    return Response.IndustriesResponse(body)
                }

            }
            is VacanciesRequestDto -> {
                val resp = endpointsApiService.vacancies(dto.toQueryMap()).execute()
                val body = resp.body()
                if (body != null) {
                    return Response.VacanciesResponse(body)
                }

            }
            is VacancyDetailsRequestDto -> {
                val resp = endpointsApiService.vacancyDetails(dto.toQueryMap()).execute()
                val body = resp.body()
                if (body != null) {
                    return Response.VacancyDetailsResponse(body)
                }
            }
        }

        return Response.ErrorResponse(Response.STATUS_UNKNOWN_ERROR)

    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
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

}
