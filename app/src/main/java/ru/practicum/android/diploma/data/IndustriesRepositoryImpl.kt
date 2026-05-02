package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.api.IndustryApiConverter
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.IndustriesRepository
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: IndustryApiConverter
) : IndustriesRepository {

    override suspend fun getAllFromApi(): List<Industry>? {
        return (networkClient.doRequest(IndustriesRequestDto()) as Response.IndustriesResponse)
            .body?.map { industryDto -> apiConverter.map(industryDto) }
    }

}
