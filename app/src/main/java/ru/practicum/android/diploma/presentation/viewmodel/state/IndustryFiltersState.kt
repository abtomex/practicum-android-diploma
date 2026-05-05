package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.Industry

sealed interface IndustryFiltersState {
    data object Default : IndustryFiltersState
    data object Loading : IndustryFiltersState
    data object Empty : IndustryFiltersState
    data class Error(val message: String) : IndustryFiltersState
    data object NoInternet : IndustryFiltersState
    data class Content(val data: List<Industry>) : IndustryFiltersState
    data class Checked(val checkedIndustry: Industry?) : IndustryFiltersState
}
