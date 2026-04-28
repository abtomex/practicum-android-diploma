package ru.practicum.android.diploma.presentation.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.domain.models.VacancyRequestByPages
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState
import ru.practicum.android.diploma.util.debounce
import java.util.stream.Collectors

class SearchViewModel(
    val searchInteractor: SearchInteractor
) : ViewModel() {

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state
    private var appliedSearchText: String = ""


    private val vacancySearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText ->
            state.postValue(SearchState.Loading)
            loadData(changedText)
        }

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            state.postValue(SearchState.Content(emptyList()))
        } else {
            vacancySearchDebounce(query)
        }
    }


    fun loadData(searchText: String) {
        loadData(searchText, FIRST_PAGE_NUMBER)
    }
    fun loadData(searchText: String, page: Int) {

        state.value = SearchState.Loading

        viewModelScope.launch {
            searchInteractor.searchByPages(
                VacancyRequestByPages(
                    text = searchText,
                    page = page
                )
            ).collect { data ->
                processData(data, searchText != appliedSearchText)
                appliedSearchText = searchText
            }

        }
    }

    private fun processData(data: ApiResponse<out List<VacancyCard>?>, isNewSearch: Boolean) {
        when (data) {
            is ApiResponse.Success -> {
                if(isNewSearch) {
                    state.postValue(SearchState.Content(data.data!!))
                } else {
                    state.postValue(SearchState.ContentNextPage(data.data!!))
                }
            }

            is ApiResponse.Error -> {
                val error = SearchState.Error(data.message)
                state.postValue(error)
            }

            is ApiResponse.NoInternet -> {
                val error = SearchState.NoInternet(data.message)
                state.postValue(error)
            }

        }
    }

    fun addUnique(existed: SnapshotStateList<VacancyCard>, arrival: List<VacancyCard>) {

        val toAdd = arrival.stream()
            .filter { arrivalItem -> !existed.map { it.id }.contains(arrivalItem.id)  }
            .collect(Collectors.toList())
        existed.addAll(toAdd)
    }

    fun loadNextPage(page: Int) {
        loadData(this.appliedSearchText, page)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val FIRST_PAGE_NUMBER = 1
    }

}
