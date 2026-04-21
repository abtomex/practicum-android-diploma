package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.IndustriesApiConverter
import ru.practicum.android.diploma.data.dto.industries.IndustriesRequestDto
import ru.practicum.android.diploma.data.dto.industries.IndustriesResponseDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.IndustriesRepository
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesRepositoryImpl (
    val networkClient: NetworkClient,
    val apiConverter: IndustriesApiConverter
): IndustriesRepository {

    override suspend fun getAllFromApi(): List<Industry> {
        return (networkClient.doRequest(IndustriesRequestDto()) as IndustriesResponseDto)
            .industries.map { industryDto -> apiConverter.map(industryDto) }
    }

}
