package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

interface SearchInteractor {
    fun searchAll(searchStr: String): Flow<ApiResponse<out List<VacancyCard>?>>
    fun searchByPages(vacancyRequestByPages: VacancyRequestByPages): Flow<ApiResponse<Vacancies>>

}
