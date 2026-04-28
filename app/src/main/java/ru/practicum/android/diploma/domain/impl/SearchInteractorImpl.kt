package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.converters.VacanciesApiConverter
import ru.practicum.android.diploma.data.converters.VacancyRequestApiConverter
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

class SearchInteractorImpl(
    val vacanciesRepository: VacanciesRepository,
    val requestApiConverter: VacancyRequestApiConverter,
    val vacanciesApiConverter: VacanciesApiConverter
) : SearchInteractor{
    override fun searchAll(searchStr: String) = flow {
        emit(vacanciesRepository.findVacanciesByStr(searchStr))

    }
        .flowOn(Dispatchers.IO)

    override fun searchByPages(vacancyRequestByPages: VacancyRequestByPages) = flow {
        emit(vacanciesRepository.searchVacancies(requestApiConverter.map(vacancyRequestByPages)))
    }
        .flowOn(Dispatchers.IO)
        .map {
            when (it) {
                is ApiResponse.Success -> return@map ApiResponse.Success(vacanciesApiConverter.map(it.data))
                is ApiResponse.Error -> return@map ApiResponse.Error(it.message)
                is ApiResponse.NoInternet -> return@map ApiResponse.NoInternet(it.message)
            }

        }
}
