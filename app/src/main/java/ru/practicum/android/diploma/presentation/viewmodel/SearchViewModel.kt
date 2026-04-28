package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Vacancies
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    val searchInteractor: SearchInteractor
) : ViewModel() {

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state

    private var appliedSearchText: String = ""
    private var currentPage: Int = 1
    private var totalPages: Int = 1

    private val vacancySearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText ->
        state.postValue(SearchState.Loading)
        loadData(changedText, FIRST_PAGE_NUMBER)
    }

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            state.postValue(SearchState.Content(emptyList()))
        } else {
            vacancySearchDebounce(query)
        }
    }

    fun loadData(searchText: String, page: Int) {
        if (page < 1) return

        state.value = SearchState.Loading
        currentPage = page

        viewModelScope.launch {
            searchInteractor.searchByPages(
                VacancyRequestByPages(
                    text = searchText,
                    page = page
                )
            ).collect { response ->
                processData(response, searchText, page)
                appliedSearchText = searchText
            }
        }
    }

    private fun processData(
        response: ApiResponse<Vacancies>,
        searchText: String,
        page: Int
    ) {
        when (response) {
            is ApiResponse.Success -> {
                val vacancies = response.data.items
                totalPages = response.data.pages

                if (searchText != appliedSearchText) {
                    // Новый поиск
                    state.postValue(SearchState.Content(vacancies, page, totalPages))
                } else {
                    // Подгрузка новой страницы
                    state.postValue(SearchState.ContentNextPage(vacancies, page, totalPages))
                }
            }
            is ApiResponse.Error -> {
                state.postValue(SearchState.Error(response.message))
            }
            is ApiResponse.NoInternet -> {
                state.postValue(SearchState.NoInternet(response.message))
            }
        }
    }

    fun loadNextPage(page: Int) {
        if (page <= totalPages && page != currentPage) {
            loadData(appliedSearchText, page)
        }
    }

    fun loadPreviousPage(page: Int) {
        if (page >= 1 && page != currentPage) {
            loadData(appliedSearchText, page)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val FIRST_PAGE_NUMBER = 1
    }
}
