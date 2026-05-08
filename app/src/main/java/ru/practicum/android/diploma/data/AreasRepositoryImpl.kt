package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.domain.AreasRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Area

class AreasRepositoryImpl : AreasRepository {

    override suspend fun getAllFromApi(): ApiResponse<List<Area>?> {
        TODO()
    }
}
