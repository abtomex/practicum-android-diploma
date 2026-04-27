package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard

interface SearchInteractor {
    fun searchAll(searchStr: String): Flow<ApiResponse<out List<VacancyCard>?>>

}
