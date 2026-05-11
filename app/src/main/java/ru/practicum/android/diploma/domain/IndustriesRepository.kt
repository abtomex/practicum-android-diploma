package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesRepository {
    suspend fun getAllFromApi(): ApiResponse<List<Industry>?>
}
