package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

interface SearchInteractor {
    fun searchByPages(vacancyRequestByPages: VacancyRequestByPages): Flow<ApiResponse<Vacancies>>

}
