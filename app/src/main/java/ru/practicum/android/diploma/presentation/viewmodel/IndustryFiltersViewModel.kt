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

    private val state = MutableStateFlow<IndustryFiltersState>(IndustryFiltersState.Default)
    val stateModified: StateFlow<IndustryFiltersState> = state.asStateFlow()
    private val listIndustries = MutableStateFlow<List<Industry>>(emptyList())
    val listIndustriesModified: StateFlow<List<Industry>> = listIndustries.asStateFlow()
    private val industryFilter = MutableStateFlow<MutableList<Industry>>(mutableListOf())
    val listIndustryFilterModified: StateFlow<List<Industry>> = industryFilter.asStateFlow()

    fun getIndustriesList() {
        viewModelScope.launch {
            industryInteractor.getIndustriesList()
                .collect {
                    listIndustries.value = it ?: emptyList()
                    Log.d(LOADED_INDUSTRIES, it.toString())
                }
            state.value = IndustryFiltersState.Content(
                data = listIndustries.value
            )
        }

    }

    fun addFilter(industry: Industry) {
        industryFilter.value.add(industry)
    }

    companion object {
        private const val LOADED_INDUSTRIES = "loadedIndustries"
    }

// todo: когда пользователь выбрал (не выбрал) отрасли, при нажатии на кнопку Выбрать. state становится
//  IndustryFiltersState.Checked с переданным в него объектом отрасли
//    и выполняется возвращение на предыдущий экран navController.navigateUp()

}
