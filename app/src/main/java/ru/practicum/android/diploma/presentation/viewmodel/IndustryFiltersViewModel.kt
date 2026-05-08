package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.api.ApiResponse
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState

class IndustryFiltersViewModel(
    private val industryInteractor: IndustriesInteractor
) : ViewModel() {

    private val stateChangeable = MutableStateFlow<IndustryFiltersState>(IndustryFiltersState.Default)
    val state: StateFlow<IndustryFiltersState> = stateChangeable.asStateFlow()
    private val industryFilterChangeable = MutableStateFlow<Industry?>(null)

    fun getIndustriesList() {
        viewModelScope.launch {
            stateChangeable.value = IndustryFiltersState.Loading
            industryInteractor.getIndustriesList()
                .collect {
                    when (it) {
                        is ApiResponse.Success -> stateChangeable.value = IndustryFiltersState.Content(
                            data = it.data ?: emptyList()
                        )
                        is ApiResponse.Error -> stateChangeable.value = IndustryFiltersState.Error(it.message)
                        is ApiResponse.NoInternet -> stateChangeable.value = IndustryFiltersState.NoInternet
                    }

                    Log.d(LOADED_INDUSTRIES, it.toString())
                }
        }

    }

    fun addFilter(industry: Industry) {
        industryFilterChangeable.value = industry
    }

    companion object {
        private const val LOADED_INDUSTRIES = "loadedIndustries"
    }
}
