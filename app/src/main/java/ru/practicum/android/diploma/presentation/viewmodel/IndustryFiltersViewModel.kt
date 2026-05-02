package ru.practicum.android.diploma.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.usecases.IndustriesInteractor
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState

class IndustryFiltersViewModel(
    private val industryInteractor: IndustriesInteractor
) : ViewModel() {

    val _state = MutableStateFlow<IndustryFiltersState>(IndustryFiltersState.Default)
    val state: StateFlow<IndustryFiltersState> = _state.asStateFlow()
    val _listIndustries = MutableStateFlow<List<Industry>>(emptyList())
    val listIndustries: StateFlow<List<Industry>> = _listIndustries.asStateFlow()
    val _industryFilter = MutableStateFlow<Industry?>(null)
    val industryFilter: StateFlow<Industry?> = _industryFilter.asStateFlow()

    init {
        getIndustriesList()
    }

    fun getIndustriesList() {
        viewModelScope.launch {
            _listIndustries.value = (industryInteractor.getIndustriesList() ?: emptyList())
            _state.value = IndustryFiltersState.Content(
                data = _listIndustries.value,
                checked = null
            )
        }

    }

    fun setFilter(industry: Industry) {
        _industryFilter.value = industry
        _state.value = IndustryFiltersState.Content(
            data = _listIndustries.value,
            checked = industry
        )
    }

}
