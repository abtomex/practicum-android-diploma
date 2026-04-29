package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface SearchState {

    data object Default : SearchState
    data object Loading : SearchState
    data object Empty : SearchState
    data class Error(val message: String) : SearchState
    data object NoInternet : SearchState
    data class Content(
        val data: List<VacancyCard>,
        val found: Int,
        val isNextPageLoading: Boolean = false
    ) : SearchState

}
