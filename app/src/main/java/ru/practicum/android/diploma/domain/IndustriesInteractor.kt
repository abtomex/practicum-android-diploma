package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesInteractor {
    suspend fun getIndustriesList(): Flow<ApiResponse<List<Industry>?>>
}
