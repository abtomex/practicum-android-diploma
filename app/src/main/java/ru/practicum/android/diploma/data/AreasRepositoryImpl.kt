package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.AreasApiConverter
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.dto.areas.AreasResponseDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.AreasRepository
import ru.practicum.android.diploma.domain.models.Area

class AreasRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: AreasApiConverter
) : AreasRepository {

    override suspend fun getAllFromApi(): List<Area> {
        return (networkClient.doRequest(AreasRequestDto()) as AreasResponseDto)
            .areas.map { areaDto -> apiConverter.map(areaDto) }
    }
}
