package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages
import ru.practicum.android.diploma.presentation.viewmodel.state.AppliedFilters
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchFailuresEnum
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    val searchInteractor: SearchInteractor,
) : ViewModel() {

    private var appliedFilters: AppliedFilters? = null
    private val _isNoInternet = MutableStateFlow(false)

    private val _state = MutableStateFlow<SearchState>(SearchState.Default)
    val state: StateFlow<SearchState> = _state.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchQuery.asStateFlow()

    private val _toastEvent = MutableSharedFlow<SearchFailuresEnum>()
    val toastEvent: SharedFlow<SearchFailuresEnum> = _toastEvent.asSharedFlow()

    private var currentPage = 0
    private var maxPages = 0
    private var currentVacancies = mutableListOf<VacancyCard>()
    private var isNextPageLoading = false

    private val searchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY_MILLIS,
        coroutineScope = viewModelScope
    ) { query ->
        searchRequest(query)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            currentVacancies.clear()
            currentPage = 0
            maxPages = 0
            _state.value = SearchState.Default
        } else {
            _state.value = SearchState.Loading
            searchDebounce(query)
        }
    }

    fun onClearSearch() {
        _searchQuery.value = ""
        currentVacancies.clear()
        currentPage = 0
        maxPages = 0
        _state.value = SearchState.Default
    }

    fun onLoadNextPage() {
        if (!canLoadNextPage()) return

        startNextPageLoading()
        viewModelScope.launch {
            searchInteractor.searchByPages(
                VacancyRequestByPages(
                    text = _searchQuery.value,
                    page = currentPage + 1
                    // todo: делаем обогащение VacancyRequestByPages из стейта фильтров
                ),

            )
                .collect { result ->
                    handleNextPageResult(result)
                }
        }
    }

    private fun searchRequest(query: String) {
        viewModelScope.launch {
            _isNoInternet.value = false
            currentPage = 0
            currentVacancies.clear()
            searchInteractor.searchByPages(
                VacancyRequestByPages(
                    text = query,
                    page = currentPage + 1,
                    industry = appliedFilters?.industry,
                    salary = appliedFilters?.salary,
                    onlyWithSalary = appliedFilters?.onlyWithSalary ?: false
                ),

            ).collect { result ->
                handleSearchResult(result)
            }
        }
    }

    private fun canLoadNextPage(): Boolean {
        return !isNextPageLoading && currentPage + 1 < maxPages
    }

    private fun startNextPageLoading() {
        isNextPageLoading = true
        val currentState = _state.value
        if (currentState is SearchState.Content) {
            _state.value = currentState.copy(isNextPageLoading = true)
        }
    }

    private suspend fun handleNextPageResult(result: ApiResponse<Vacancies>) {
        when (result) {
            is ApiResponse.Success -> onNextPageSuccess(result.data)
            is ApiResponse.Error -> onNextPageError(result.code)
            is ApiResponse.NoInternet -> onNextPageError(result.code)
        }
        isNextPageLoading = false
    }

    private fun onNextPageSuccess(data: Vacancies) {
        currentPage = data.page
        maxPages = data.pages
        currentVacancies.addAll(data.items)
        _state.value = SearchState.Content(
            data = currentVacancies.toList(),
            found = data.found,
            isNextPageLoading = false
        )
    }

    private suspend fun onNextPageError(code: Int?) {
        val currentState = _state.value
        if (currentState is SearchState.Content) {
            _state.value = currentState.copy(isNextPageLoading = false)
        }
        emitToastByCode(code)
    }

    private fun handleSearchResult(result: ApiResponse<Vacancies>) {
        when (result) {
            is ApiResponse.Success -> onSearchSuccess(result.data)
            is ApiResponse.Error -> onSearchError(result.code, result.message)
            is ApiResponse.NoInternet -> onSearchError(result.code, result.message)
        }
    }

    private fun onSearchSuccess(data: Vacancies) {
        maxPages = data.pages
        currentVacancies.addAll(data.items)
        _state.value = if (data.items.isEmpty()) {
            SearchState.Empty
        } else {
            SearchState.Content(
                data = currentVacancies.toList(),
                found = data.found
            )
        }
    }

    private fun onSearchError(code: Int?, message: String?) {
        _state.value = if (code == NO_INTERNET_CODE) {
            SearchState.NoInternet
        } else {
            SearchState.Error(message ?: "")
        }
    }

    private suspend fun emitToastByCode(code: Int?) {
        if (code == NO_INTERNET_CODE) {
            _toastEvent.emit(SearchFailuresEnum.NO_INTERNET)
        } else {
            _toastEvent.emit(SearchFailuresEnum.SERVER_ERROR)
        }
    }

    fun updateFilters(appliedFilters: AppliedFilters) {
        this.appliedFilters = appliedFilters
        searchRequest(_searchQuery.value)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val NO_INTERNET_CODE = -1

    }

}
