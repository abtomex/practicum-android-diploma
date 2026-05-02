package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface AreaFiltersState {
    data object Default : AreaFiltersState
    data object Loading : AreaFiltersState
    data object Empty : AreaFiltersState
    data class Error(val message: String) : AreaFiltersState
    data object NoInternet : AreaFiltersState
    data class Content(
        val data: List<Area>,
        val checked: Area?
    ) : AreaFiltersState
}
