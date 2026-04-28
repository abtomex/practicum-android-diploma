package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.converters.VacancyRequestApiConverter
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.VacanciesRepository
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

class SearchInteractorImpl(
    val vacanciesRepository: VacanciesRepository,
    val requestApiConverter: VacancyRequestApiConverter
) : SearchInteractor{
    override fun searchAll(searchStr: String) = flow {
        emit(vacanciesRepository.findVacanciesByStr(searchStr))

    }
        .flowOn(Dispatchers.IO)

    override fun searchByPages(vacancyRequestByPages: VacancyRequestByPages) = flow {
        emit(vacanciesRepository.searchVacancies(requestApiConverter.map(vacancyRequestByPages)))

    }
        .flowOn(Dispatchers.IO)
}
