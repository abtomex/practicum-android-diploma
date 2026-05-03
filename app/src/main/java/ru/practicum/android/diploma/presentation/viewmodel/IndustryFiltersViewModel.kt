package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.impl.IndustriesInteractorImpl
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState

class IndustryFiltersViewModel(
    private val industryInteractor: IndustriesInteractorImpl
) : ViewModel() {

    private val state = MutableStateFlow<IndustryFiltersState>(IndustryFiltersState.Default)
    val stateModified: StateFlow<IndustryFiltersState> = state.asStateFlow()
    private val listIndustries = MutableStateFlow<List<Industry>>(emptyList())
    val listIndustriesModified: StateFlow<List<Industry>> = listIndustries.asStateFlow()
    private val industryFilter = MutableStateFlow<MutableList<Industry>>(mutableListOf())
    val listIndustryFilterModified: StateFlow<List<Industry>> = industryFilter.asStateFlow()

    init {
        getIndustriesList()
    }

    fun getIndustriesList() {
        viewModelScope.launch {
            listIndustries.value = industryInteractor.getIndustriesList() ?: emptyList()
            state.value = IndustryFiltersState.Content(
                data = listIndustries.value
            )
        }

    }

    fun addFilter(industry: Industry) {
        industryFilter.value.add(industry)
    }

}
