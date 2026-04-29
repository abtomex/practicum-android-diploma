package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import ru.practicum.android.diploma.data.dto.Request
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreaDto
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustryDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesDto
import ru.practicum.android.diploma.data.dto.vacancies.VacanciesRequestDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsDto
import ru.practicum.android.diploma.data.dto.vacancydetails.VacancyDetailsRequestDto

class RetrofitNetworkClient(
    private val endpointsApiService: EndpointsApiService,
    private val context: Context

) : NetworkClient {

    override suspend fun doRequest(dto: Request): Response<out Any> {
        if (!isConnected()) {
            return Response.ErrorResponse(errorCode = Response.STATUS_NETWORK_ERROR)
        }
        val errCode = try {
            return when (dto) {
                is AreasRequestDto -> processAreasRequest()
                is IndustriesRequestDto -> processIndustriesRequest()
                is VacanciesRequestDto -> processVacanciesRequest<VacanciesDto>(dto)

                is VacancyDetailsRequestDto -> processVacancyDetailsRequest(dto)
                else -> throw RuntimeException("unexpected request dto")
            }
        } catch (_: Exception) {
            500
        }

        return Response.ErrorResponse(errCode)

    }

    private fun processVacancyDetailsRequest(dto: VacancyDetailsRequestDto): Response<VacancyDetailsDto> {
        val resp = endpointsApiService.vacancyDetails(dto.toQueryMap()).execute()
        val body = resp.body()
        return if (body != null) {
            Response.VacancyDetailsResponse(body)
        } else {
            Response.ErrorResponse(resp.code()) as Response<VacancyDetailsDto>
        }
    }

    private fun processIndustriesRequest(): Response<List<IndustryDto>> {
        val resp = endpointsApiService.industries().execute()
        val body = resp.body()
        return if (body != null) {
            Response.IndustriesResponse(body)
        } else {
            Response.ErrorResponse(resp.code()) as Response<List<IndustryDto>>
        }

    }

    private fun processAreasRequest(): Response<List<AreaDto>> {
        val resp = endpointsApiService.areas().execute()
        val body = resp.body()
        return if (body != null) {
            Response.AreasResponse(body)
        } else {
            Response.ErrorResponse(resp.code()) as Response<List<AreaDto>>
        }
    }

    private fun <T> processVacanciesRequest(dto: VacanciesRequestDto): Response<T> {
        val resp = endpointsApiService.vacancies(dto.toQueryMap()).execute()
        val body = resp.body()
        return if (body != null) {
            Response.VacanciesResponse(body) as Response<T>
        } else {
            Response.ErrorResponse(resp.code()) as Response<T>
        }

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
