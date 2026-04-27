package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.VacanciesRepository

class SearchInteractorImpl(
    val vacanciesRepository: VacanciesRepository
) : SearchInteractor{
    override fun searchAll(searchStr: String) = flow {
        emit(vacanciesRepository.findVacanciesByStr(searchStr))

    }
        .flowOn(Dispatchers.IO)
}
