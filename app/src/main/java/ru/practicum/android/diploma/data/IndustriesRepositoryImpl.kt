package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.api.IndustryApiConverter
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.IndustriesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: IndustryApiConverter
) : IndustriesRepository {

    override suspend fun getAllFromApi(): ApiResponse<List<Industry>?> {
        val resp = networkClient.doRequest(IndustriesRequestDto())
        val resultResponse: ApiResponse<List<Industry>?> = when (resp) {
            is Response.IndustriesResponse -> ApiResponse.Success(resp.body?.map { apiConverter.map(it) })
            is Response.ErrorResponse -> {
                if (resp.errorCode == Response.STATUS_NETWORK_ERROR) ApiResponse.NoInternet(
                    Response.NO_INTERNET_MSG,
                    Response.STATUS_NETWORK_ERROR
                )
                else ApiResponse.Error("", resp.errorCode)
            }

            else -> ApiResponse.Success(emptyList())
        }
        return resultResponse
    }

}
