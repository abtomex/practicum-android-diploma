package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface SearchState {

    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class NoInternet(val message: String) : SearchState
    data class Content(
        val data: List<VacancyCard>,
        val page: Int = 1,
        val totalPages: Int = 1
    ) : SearchState
    data class ContentNextPage(
        val data: List<VacancyCard>,
        val page: Int = 1,
        val totalPages: Int = 1
    ) : SearchState

}
