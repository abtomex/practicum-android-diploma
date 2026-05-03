package ru.practicum.android.diploma.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.IndustriesInteractor
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.presentation.viewmodel.state.IndustryFiltersState

class IndustryFiltersViewModel(
    private val industryInteractor: IndustriesInteractor
) : ViewModel() {

    private val stateChangeable = MutableStateFlow<IndustryFiltersState>(IndustryFiltersState.Default)
    val state: StateFlow<IndustryFiltersState> = stateChangeable.asStateFlow()
    private val listIndustriesChangeable = MutableStateFlow<List<Industry>>(emptyList())
    val listIndustries: StateFlow<List<Industry>> = listIndustriesChangeable.asStateFlow()
    private val industryFilterChangeable = MutableStateFlow<Industry?>(null)
    val listIndustryFilter: StateFlow<Industry?> = industryFilterChangeable.asStateFlow()

//    init {
//        getIndustriesList()
//    }

    fun getIndustriesList() {
        viewModelScope.launch {
            industryInteractor.getIndustriesList()
                .collect {
                    listIndustriesChangeable.value = it ?: emptyList()
                    Log.d(LOADED_INDUSTRIES, it.toString())
                }
            stateChangeable.value = IndustryFiltersState.Content(
                data = listIndustries.value
            )
        }

    }

    fun addFilter(industry: Industry) {
        industryFilterChangeable.value = industry
    }

    fun confirmFilter() {
        stateChangeable.value = IndustryFiltersState.Checked(
            checkedIndustry = industryFilterChangeable.value
        )
    }

    companion object {
        private const val LOADED_INDUSTRIES = "loadedIndustries"
    }
}
