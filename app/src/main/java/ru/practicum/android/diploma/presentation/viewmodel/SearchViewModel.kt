package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.VacancyCard
import ru.practicum.android.diploma.presentation.viewmodel.state.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    val searchInteractor: SearchInteractor
) : ViewModel() {

    private val state = MutableLiveData<SearchState>()
    fun getState(): LiveData<SearchState> = state


    private val vacancySearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope) { changedText ->
            loadData(changedText)
        }

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) {
            state.postValue(SearchState.Content(emptyList()))
        } else {
            state.postValue(SearchState.Loading)
            vacancySearchDebounce(query)
        }
    }


    fun loadData(searchText: String) {

        state.value = SearchState.Loading

        viewModelScope.launch {
            searchInteractor.searchAll(
                searchStr = searchText
            ).collect { data -> processData(data) }

        }
    }

    private fun processData(data: ApiResponse<out List<VacancyCard>?>) {
        when (data) {
            is ApiResponse.Success -> {
                val content = SearchState.Content(data.data!!)
                state.postValue(content)
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

//    fun scheduleSearch(trimmedText: String) {
//
//        searchJob?.cancel()
//        searchJob = viewModelScope.launch {
//            delay(SEARCH_DEBOUNCE_DELAY)
//            doSearch(searchTrack)
//        }
//    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }

}
