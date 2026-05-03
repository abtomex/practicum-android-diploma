package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.VacancyRequestByPages

sealed interface SearchFiltersState {
    data object Default : SearchFiltersState
    data object Loading : SearchFiltersState
    data class SpecifiedFilters(val filters: VacancyRequestByPages) : SearchFiltersState
    // todo: можно обновлять и дополнять в конце удалить эту туду
}
