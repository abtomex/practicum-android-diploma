package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Area

interface AreasRepository {
    suspend fun getAllFromApi(): ApiResponse<List<Area>?>
}
