package ru.practicum.android.diploma.domain.usecases

import ru.practicum.android.diploma.domain.IndustriesRepository

class IndustriesInteractor(
    private val industriesRepository: IndustriesRepository
) {
    suspend fun getIndustriesList() = industriesRepository.getAllFromApi()
}
