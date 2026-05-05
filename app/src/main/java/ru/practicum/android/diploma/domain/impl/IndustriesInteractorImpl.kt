package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.IndustriesRepository

class IndustriesInteractorImpl(
    private val industriesRepository: IndustriesRepository
) : IndustriesInteractor {
    override suspend fun getIndustriesList() = flow {
        emit(industriesRepository.getAllFromApi())
    }.flowOn(Dispatchers.IO)
}
