package ru.practicum.android.diploma.presentation.viewmodel.state

import ru.practicum.android.diploma.domain.models.VacancyCard

sealed interface SearchState {

    data object Loading : SearchState
    data class Error(val message: String) : SearchState
    data class NoInternet(val message: String) : SearchState
    data class Content(val data: List<VacancyCard>) : SearchState
    class ContentNextPage(val data: List<VacancyCard>) : SearchState

}
