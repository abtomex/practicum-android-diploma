package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.IndustriesRepository

class IndustriesInteractorImpl(
    private val industriesRepository: IndustriesRepository
) : IndustriesInteractor {
    override suspend fun getIndustriesList() = industriesRepository.getAllFromApi()
}
