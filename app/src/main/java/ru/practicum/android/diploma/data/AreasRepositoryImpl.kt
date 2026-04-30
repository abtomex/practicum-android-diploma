package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.converters.api.AreaApiConverter
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.areas.AreasRequestDto
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.AreasRepository
import ru.practicum.android.diploma.domain.models.Area

class AreasRepositoryImpl(
    val networkClient: NetworkClient,
    val apiConverter: AreaApiConverter
) : AreasRepository {

    override suspend fun getAllFromApi(): List<Area>? {
        return (networkClient.doRequest(AreasRequestDto()) as Response.AreasResponse)
            .body?.map { areaDto -> apiConverter.map(areaDto) }
    }
}
