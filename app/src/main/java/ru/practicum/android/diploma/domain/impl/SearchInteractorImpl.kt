package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.api.VacanciesApiConverter
import ru.practicum.android.diploma.data.converters.api.VacancyRequestApiConverter
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

class SearchInteractorImpl(
    val vacanciesRepository: VacanciesRepository,
    val requestApiConverter: VacancyRequestApiConverter,
    val vacanciesApiConverter: VacanciesApiConverter
) : SearchInteractor {

    override fun searchByPages(vacancyRequestByPages: VacancyRequestByPages) = flow {
        emit(vacanciesRepository.searchVacancies(requestApiConverter.map(vacancyRequestByPages)))
    }
        .flowOn(Dispatchers.IO)
        .map { response ->
            when (response) {
                is ApiResponse.Success -> ApiResponse.Success(vacanciesApiConverter.map(response.data))
                is ApiResponse.Error -> ApiResponse.Error(response.message, response.code)
                is ApiResponse.NoInternet -> ApiResponse.NoInternet(response.message, response.code)
            }
        }
}
