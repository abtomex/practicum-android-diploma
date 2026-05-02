package ru.practicum.android.diploma.domain.usecases

import ru.practicum.android.diploma.domain.AreasRepository

class AreaFiltersViewModel(
    private val areasRepository: AreasRepository
) {
    suspend fun getAreasList() = areasRepository.getAllFromApi()
}
